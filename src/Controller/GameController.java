package Controller;
import java.awt.Rectangle;
import Model.*;
import View.*;
import Config.GameConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;

/**
 * Controller principale del gioco.
 * Gestisce il game loop, il flusso tra i livelli e collega Model e View.
 * Implementa Observer per reagire agli eventi del Model (morti, game over).
 */
public class GameController implements Runnable, Observer {

    /** Frame al secondo target. */
    private static final int FPS = 60;

    /** Millisecondi per frame. */
    private static final long TARGET_TIME = 1000 / FPS;

    /** Riferimento a Link. */
    private Link link;

    /** Riferimento alla View principale. */
    private GameView view;

    /** Riferimento al profilo utente. */
    private Profilo profilo;

    /** Gestore degli input da tastiera. */
    private InputHandler inputHandler;

    /** Gestore delle collisioni. */
    private CollisionController collisionController;

    /** Livello corrente. */
    private Livello livelloCorrente;

    /** Numero del livello corrente. */
    private int numeroLivelloCorrente;

    /** Stato corrente del gioco. */
    private GameState statoGioco;

    /** Lista dei coniglietti attivi nella stanza. */
    private List<Coniglietti> coniglietti;

    /** Nickname del giocatore corrente. */
    private String nicknameGiocatore;

    /** Thread del game loop. */
    private Thread gameThread;

    /**
     * Costruttore di GameController.
     *
     * @param link         il personaggio principale
     * @param view         la view principale
     * @param profilo      il profilo utente
     * @param inputHandler il gestore degli input
     */
    public GameController(Link link, GameView view, Profilo profilo, InputHandler inputHandler) {
        this.link = link;
        this.view = view;
        this.profilo = profilo;
        this.inputHandler = inputHandler;
        this.collisionController = new CollisionController(link);
        this.coniglietti = new ArrayList<>();
        this.statoGioco = GameState.MENU;
        this.numeroLivelloCorrente = 1;
        setupListeners();
    }

    /**
     * Collega i pulsanti della View alle azioni del Controller.
     */
    private void setupListeners() {
        view.getMenuPanel().getBtnGioca().addActionListener(e -> iniziaPartita());
        view.getMenuPanel().getBtnProfilo().addActionListener(e -> view.mostraPanel("PROFILO"));
        view.getMenuPanel().getBtnClassifica().addActionListener(e -> {
            view.getLeaderboardPanel().aggiorna();
            view.mostraPanel("CLASSIFICA");
        });
        view.getMenuPanel().getBtnEsci().addActionListener(e -> System.exit(0));

        view.getProfilePanel().getBtnCreaProfilo().addActionListener(e -> {
            String nick = view.getProfilePanel().getNickname();
            if (!nick.isEmpty()) {
                profilo.aggiungiProfilo(nick, "");
                view.getProfilePanel().aggiorna();
            }
        });
        view.getProfilePanel().getBtnSeleziona().addActionListener(e -> {
            String nick = view.getProfilePanel().getNicknameSelezionato();
            if (nick != null) {
                nicknameGiocatore = nick;
                view.mostraPanel("MENU");
            }
        });
        view.getProfilePanel().getBtnIndietro().addActionListener(e -> view.mostraPanel("MENU"));

        view.getLeaderboardPanel().getBtnIndietro().addActionListener(e -> view.mostraPanel("MENU"));

        view.getShopPanel().getBtnAcquistaCarota().addActionListener(e -> {
            new Shop().acquista(link);
            view.getShopPanel().aggiorna();
        });
        view.getShopPanel().getBtnContinua().addActionListener(e -> prossimoLivello());

        view.getGameOverPanel().getBtnRigioca().addActionListener(e -> iniziaPartita());
        view.getGameOverPanel().getBtnMenu().addActionListener(e -> {
            statoGioco = GameState.MENU;
            view.mostraPanel("MENU");
        });

        view.getVittoriaPanel().getBtnMenu().addActionListener(e -> {
            statoGioco = GameState.MENU;
            view.mostraPanel("MENU");
        });
    }
    

    /**
     * Avvia una nuova partita resettando lo stato del gioco.
     */
    public void iniziaPartita() {
        numeroLivelloCorrente = 1;
        Link.VITE = 3;
        link.reset();
        coniglietti = new ArrayList<>();
        statoGioco = GameState.GIOCO;
        caricaLivello(numeroLivelloCorrente);
        view.mostraPanel("GIOCO");
        view.getGamePanel().requestFocusInWindow();
        avviaGameLoop();
    }

    /**
     * Carica il livello corrispondente al numero dato.
     *
     * @param numero numero del livello da caricare
     */
    private void caricaLivello(int numero) {
        livelloCorrente = switch (numero) {
            case 1 -> new Livello1();
            case 2 -> new Livello2();
            case 3 -> new Livello3();
            case 4 -> new Livello4();
            case 5 -> new Livello5();
            case 6 -> new Livello6();
            case 7 -> new Livello7();
            case 8 -> new Livello8();
            default -> new Livello1();
        };
        for (NemicoBase n : livelloCorrente.getNemiciStanzaCorrente()) {
            n.addObserver(this);
        }
        livelloCorrente.getBoss().addObserver(this);
        view.setLivello(livelloCorrente);
        view.getGamePanel().setConiglietti(coniglietti);
        AudioManager.getInstance().loop("dungeon.wav");
    }

    /**
     * Avanza al livello successivo o mostra la vittoria se siamo all'ultimo.
     */
    private void prossimoLivello() {
        link.aggiungiVita();
        coniglietti.clear();
        if (numeroLivelloCorrente >= GameConstants.NUM_LIVELLI) {
            profilo.aggiornaStatistiche(getNickname(), true);
            statoGioco = GameState.VITTORIA;
            view.mostraPanel("VITTORIA");
            AudioManager.getInstance().play("vittoria.wav");
        } else {
            numeroLivelloCorrente++;
            caricaLivello(numeroLivelloCorrente);
            statoGioco = GameState.GIOCO;
            view.mostraPanel("GIOCO");
            avviaGameLoop();
            SwingUtilities.invokeLater(() -> view.getGamePanel().requestFocusInWindow());
        }
    }

    /**
     * Avvia il thread del game loop.
     */
    private void avviaGameLoop() {
        if (gameThread != null && gameThread.isAlive()) gameThread.interrupt();
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Game loop — aggiorna lo stato del gioco a 60 FPS.
     */
    @Override
    public void run() {
        while (statoGioco == GameState.GIOCO) {
            long start = System.currentTimeMillis();
            aggiornaGioco();
            view.getGamePanel().repaint();
            long sleep = TARGET_TIME - (System.currentTimeMillis() - start);
            if (sleep > 0) {
                try { Thread.sleep(sleep); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }

    /**
     * Aggiorna tutto lo stato del gioco per un frame.
     */
    private void aggiornaGioco() {
        gestisciInput();
        aggiornaNemici();
        aggiornaBoss();
        aggiornaConiglietti();
        collisionController.gestisciCollisioni(livelloCorrente, coniglietti);
        verificaAvanzamentoStanza();
    }

    /**
     * Legge gli input e aggiorna lo stato di Link.
     */
    private void gestisciInput() {
        Direzione nuovaDirezione = null;
        if (inputHandler.suPremuto())            nuovaDirezione = Direzione.SU;
        else if (inputHandler.giuPremuto())      nuovaDirezione = Direzione.GIU;
        else if (inputHandler.sinistraPremuto()) nuovaDirezione = Direzione.SINISTRA;
        else if (inputHandler.destroPremuto())   nuovaDirezione = Direzione.DESTRA;

        if (nuovaDirezione != null) {
            link.setDirezione(nuovaDirezione);
            int dx = 0, dy = 0;
            switch (nuovaDirezione) {
                case SU       -> dy = -GameConstants.VELOCITA_LINK;
                case GIU      -> dy =  GameConstants.VELOCITA_LINK;
                case SINISTRA -> dx = -GameConstants.VELOCITA_LINK;
                case DESTRA   -> dx =  GameConstants.VELOCITA_LINK;
            }
            List<Personaggio> altri = new ArrayList<>(livelloCorrente.getNemiciStanzaCorrente());
            if (livelloCorrente.getBoss() != null && livelloCorrente.getBoss().isVivo())
                altri.add(livelloCorrente.getBoss());
            if (collisionController.puoMuoversi(link, dx, dy,
                    livelloCorrente.getOstacoliStanzaCorrente(), altri))
                link.muovi();
        }

        if (inputHandler.attaccoPremuto()) link.attacca(); else link.terminaAttacco();
        if (inputHandler.scudoPremuto())   link.attivaScudo(); else link.disattivaScudo();
        if (inputHandler.carotaPremuta())  link.usaCarota();
    }
    /**
     * Aggiorna il movimento dei nemici base della stanza corrente.
     */
    private void aggiornaNemici() {
        List<NemicoBase> nemici = livelloCorrente.getNemiciStanzaCorrente();
        List<Rectangle> ostacoli = livelloCorrente.getOstacoliStanzaCorrente();

        for (NemicoBase n : nemici) {
            if (!n.isVivo()) continue;
            Direzione dir = n.getDirezioneEffettiva();
            int dx = 0, dy = 0;
            switch (dir) {
                case SU       -> dy = -GameConstants.VELOCITA_NEMICO;
                case GIU      -> dy =  GameConstants.VELOCITA_NEMICO;
                case SINISTRA -> dx = -GameConstants.VELOCITA_NEMICO;
                case DESTRA   -> dx =  GameConstants.VELOCITA_NEMICO;
            }
            List<Personaggio> altri = new ArrayList<>();
            altri.add(link);
            for (NemicoBase altro : nemici)
                if (altro != n && altro.isVivo()) altri.add(altro);
            if (collisionController.puoMuoversi(n, dx, dy, ostacoli, altri))
                n.muovi();
        }
    }

    /**
     * Aggiorna il boss della stanza 3 (indice 2).
     * Gestisce sia boss mischia che boss a distanza.
     */
    private void aggiornaBoss() {
        if (livelloCorrente.getStanzaCorrente() != 2) return;
        Nemico boss = livelloCorrente.getBoss();
        if (boss == null || !boss.isVivo()) return;

        if (boss instanceof BossMischia bm) bm.aggiorna(link.getX(), link.getY());

        if (boss instanceof BossDistanza bd) {
            bd.spara(link.getX(), link.getY());
            bd.rimuoviProiettiliInattivi();
            for (Proiettile p : bd.getProiettili()) p.muovi();
        }
    }

    /**
     * Aggiorna il movimento dei coniglietti attivi e rimuove quelli non più attivi.
     */
    private void aggiornaConiglietti() {
        for (Coniglietti c : coniglietti) {
            if (!c.isAttivo()) continue;
            if (c.isSpeciale()) c.muoviVerso(link.getX(), link.getY());
            else c.muoviLontanoDa(link.getX(), link.getY());
        }
        coniglietti.removeIf(c -> !c.isAttivo());
    }

    /**
     * Controlla se la stanza corrente è completata e avanza di conseguenza.
     * Se è l'ultima stanza del livello, mostra lo shop.
     */
    private void verificaAvanzamentoStanza() {
        if (!livelloCorrente.isStanzaCompletata()) return;
        if (livelloCorrente.getStanzaCorrente() < 2) {
            livelloCorrente.prossimaStanza();
            for (NemicoBase n : livelloCorrente.getNemiciStanzaCorrente()) {
                n.addObserver(this);
            }
        } else if (livelloCorrente.isCompletato()) {
            statoGioco = GameState.SHOP;
            view.getShopPanel().aggiorna();
            view.mostraPanel("SHOP");
            AudioManager.getInstance().play("livello_completato.wav");
        }
    }

    /**
     * Riceve notifiche dal Model e reagisce agli eventi di gioco.
     *
     * @param o   il Model che ha notificato
     * @param arg stringa che identifica l'evento
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof String s)) return;
        if (s.startsWith("morte:")) {
            int rupie = Integer.parseInt(s.split(":")[1]);
            link.aggiungiRupie(rupie);
            AudioManager.getInstance().play("morte_nemico.wav");
        } else if (s.equals("morte_boss_mischia") || s.equals("morte_boss_distanza")) {
            Nemico boss = livelloCorrente.getBoss();
            List<Coniglietti> nuovi = ((SpawnaConiglietti) boss).spawnaConiglietti(boss.getX(), boss.getY());
            coniglietti.addAll(nuovi);
            view.getGamePanel().setConiglietti(coniglietti);
            AudioManager.getInstance().play("morte_boss.wav");
        } else if (s.equals("game_over")) {
            statoGioco = GameState.GAME_OVER;
            profilo.aggiornaStatistiche(getNickname(), false);
            view.mostraPanel("GAME_OVER");
            AudioManager.getInstance().play("game_over.wav");
        }
    }

    /**
     * Restituisce il nickname del giocatore corrente o "Guest" se non selezionato.
     *
     * @return nickname del giocatore
     */
    private String getNickname() {
        return nicknameGiocatore != null ? nicknameGiocatore : "Guest";
    }
}
