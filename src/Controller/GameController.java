package Controller;
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

    /**
     * Lo shop fra un livello e l'altro. E' uno solo per tutta la partita:
     * prima ne veniva creato uno nuovo a ogni acquisto, e il suo catalogo
     * finiva nel cestino subito dopo essere stato costruito.
     */
    private final Shop shop = new Shop();

    /** Nickname del giocatore corrente. */
    private String nicknameGiocatore;

    /** Thread del game loop. */
    private Thread gameThread;

    /** Schermata attualmente inquadrata (0, 1 o 2). */
    private int schermataCorrente = 0;

    /** Posizione corrente della camera in pixel di mondo. */
    private int cameraX = 0;

    /** Posizione verso cui la camera sta scorrendo. */
    private int cameraXTarget = 0;

    /** Pixel di cui la camera scorre a ogni frame durante la transizione. */
    private static final int VELOCITA_CAMERA = 8;

    /** Pixel di cui Link avanza nella schermata nuova durante lo scorrimento. */
    private static final int AVANZAMENTO_LINK = GameConstants.TILE_SIZE;

    /** Frame trascorsi dall'inizio della transizione corrente. */
    private int frameTransizione = 0;

    /** True da quando Link e' entrato nell'arena del boss. */
    private boolean bossAttivato = false;

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
        link.addObserver(this);
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
                profilo.aggiungiProfilo(nick, view.getProfilePanel().getAvatarScelto());
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
            shop.acquista(link);
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
        for (NemicoBase n : livelloCorrente.getNemici()) {
            n.addObserver(this);
        }
        livelloCorrente.getBoss().addObserver(this);

        inputHandler.azzera();
        link.riposiziona();
        schermataCorrente = 0;
        cameraX = 0;
        cameraXTarget = 0;
        frameTransizione = 0;
        bossAttivato = false;
        view.getGamePanel().setCameraX(0);

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
        Thread mio = Thread.currentThread();
        while (statoGioco == GameState.GIOCO && gameThread == mio) {
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
        if (inTransizione()) {
            avanzaTransizione();
            return;
        }
        gestisciInput();
        aggiornaNemici();
        aggiornaBoss();
        aggiornaConiglietti();
        collisionController.gestisciCollisioni(livelloCorrente, coniglietti);
        livelloCorrente.rimuoviOggettiRaccolti();
        aggiornaCamera();
        verificaCompletamentoLivello();
    }

    /**
     * Indica se la camera sta scorrendo fra due schermate.
     *
     * @return true se la camera sta scorrendo fra due schermate
     */
    private boolean inTransizione() {
        return cameraX != cameraXTarget;
    }

    /**
     * Fa scorrere la camera di un passo verso la schermata di destinazione.
     */
    private void avanzaTransizione() {
        int distanza = cameraXTarget - cameraX;
        int verso = (distanza > 0) ? 1 : -1;
        cameraX += verso * Math.min(VELOCITA_CAMERA, Math.abs(distanza));

        frameTransizione++;
        int frameTotali = GameConstants.SCHERMATA_W / VELOCITA_CAMERA;
        if (frameTransizione % (frameTotali / AVANZAMENTO_LINK) == 0) {
            link.setX(link.getX() + verso);
        }

        view.getGamePanel().setCameraX(cameraX);
        if (cameraX == cameraXTarget) frameTransizione = 0;
    }

    /**
     * Controlla se Link ha superato il confine di una schermata e in tal caso
     * avvia lo scorrimento della camera.
     * Dalla schermata del boss non si torna indietro.
     */
    private void aggiornaCamera() {
        if (schermataCorrente >= GameConstants.NUM_SCHERMATE - 1) return;

        int inizio = schermataCorrente * GameConstants.SCHERMATA_W;
        int fine   = inizio + GameConstants.SCHERMATA_W;

        int nuova = schermataCorrente;
        if (link.getX() + link.getLarghezza() > fine) nuova++;
        else if (link.getX() < inizio)            nuova--;

        if (nuova == schermataCorrente) return;
        if (nuova < 0 || nuova >= GameConstants.NUM_SCHERMATE) return;

        schermataCorrente = nuova;
        cameraXTarget = nuova * GameConstants.SCHERMATA_W;
        frameTransizione = 0;
    }

    /**
     * Impedisce a Link di uscire a sinistra dall'arena del boss.
     *
     * @param dx spostamento orizzontale richiesto
     * @return true se lo spostamento va negato
     */
    private boolean bloccatoNellArenaDelBoss(int dx) {
        if (schermataCorrente < GameConstants.NUM_SCHERMATE - 1) return false;
        return link.getX() + dx < schermataCorrente * GameConstants.SCHERMATA_W;
    }

    /**
     * Legge gli input e aggiorna lo stato di Link.
     */
    private void gestisciInput() {
      
        if (link.staMostrandoCristallo()) return;

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
            List<Personaggio> altri = new ArrayList<>();
            for (NemicoBase n : livelloCorrente.getNemici())
                if (n.isVivo()) altri.add(n);
            if (livelloCorrente.getBoss() != null && livelloCorrente.getBoss().isVivo())
                altri.add(livelloCorrente.getBoss());

            Personaggio ostacolo = collisionController.chiSullaStrada(link, dx, dy, altri);
            boolean bloccato = collisionController.fuoriDallaMappa(link, dx, dy)
                            || collisionController.bloccatoDaTile(link, dx, dy, livelloCorrente.getMappa())
                            || bloccatoNellArenaDelBoss(dx)
                            || ostacolo != null;

            if (!bloccato) link.muovi();
            if (ostacolo instanceof NemicoBase nemico) nemico.infliggiDanno(link);
        }

        if (inputHandler.attaccoPremuto()) link.attacca(); else link.terminaAttacco();
        if (inputHandler.scudoPremuto())   link.attivaScudo(); else link.disattivaScudo();
        if (inputHandler.carotaPremuta())  link.usaCarota();
    }
    /**
     * Aggiorna il movimento dei nemici base della stanza corrente.
     */
    private void aggiornaNemici() {
        List<NemicoBase> nemici = livelloCorrente.getNemici();

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
            Personaggio ostacolo = collisionController.chiSullaStrada(n, dx, dy, altri);
            boolean bloccato = collisionController.fuoriDallaMappa(n, dx, dy)
                            || collisionController.bloccatoDaTile(n, dx, dy, livelloCorrente.getMappa())
                            || ostacolo != null;

            if (!bloccato) n.muovi(); else n.rimbalza();
            if (ostacolo == link) n.infliggiDanno(link);
        }
    }

    /**
     * Aggiorna il boss, che si trova nella terza schermata.
     * Gestisce sia boss mischia che boss a distanza.
     * Il boss resta inattivo finche' Link non entra nella sua schermata.
     */
    private void aggiornaBoss() {
        if (schermataCorrente < GameConstants.NUM_SCHERMATE - 1) return;
        Nemico boss = livelloCorrente.getBoss();

        if (!bossAttivato) {
            bossAttivato = true;
            if (boss instanceof BossDistanza bd) bd.iniziaCombattimento();
        }

        if (boss == null) return;

        if (boss instanceof BossDistanza bd) {
            bd.rimuoviProiettiliInattivi();
            for (Proiettile p : bd.getProiettili()) p.muovi();
        }

        if (!boss.isVivo()) return;

        if (boss instanceof MostroTilapia t) t.aggiorna();

        if (boss instanceof BossMischia bm) bm.aggiorna(link.getX(), link.getY());

        if (boss instanceof BossDistanza bd) bd.spara(link.getX(), link.getY());
    }

    /**
     * Aggiorna il movimento dei coniglietti attivi e rimuove quelli non più attivi.
     */
    private void aggiornaConiglietti() {
        int inizio = schermataCorrente * GameConstants.SCHERMATA_W;
        int fine   = inizio + GameConstants.SCHERMATA_W;

        for (Coniglietti c : coniglietti) {
            if (!c.isAttivo()) continue;

            if (c.isSpeciale()) c.muoviVerso(link.getX(), link.getY());
            else                c.muoviLontanoDa(link.getX(), link.getY());

            if (c.getX() + c.getLarghezza() < inizio || c.getX() > fine
             || c.getY() + c.getAltezza() < 0     || c.getY() > GameConstants.MAPPA_H) {
                c.disattiva();
            }
        }
        coniglietti.removeIf(c -> !c.isAttivo());
    }

    /**
     * Controlla se il livello e' completato (boss sconfitto) e in tal caso
     * apre lo shop.
     */
    private void verificaCompletamentoLivello() {
        if (!livelloCorrente.isCompletato() || !coniglietti.isEmpty()) return;
        statoGioco = GameState.SHOP;
        view.getShopPanel().aggiorna();
        view.mostraPanel("SHOP");
        AudioManager.getInstance().play("livello_completato.wav");
    }

    /**
     * Riceve notifiche dal Model e reagisce agli eventi di gioco.
     * Gli eventi non elencati qui non riguardano il Controller: se ne occupa
     * la View, che li fa ricadere in un ridisegno.
     *
     * @param o   il Model che ha notificato
     * @param arg l'evento, una costante di Evento
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Evento evento)) return;
        switch (evento) {
            case MORTE_NEMICO -> {
                if (o instanceof NemicoBase n) {
                    livelloCorrente.creaRupia(
                        n.getX() + (n.getLarghezza()  - 10) / 2,
                        n.getY() + (n.getAltezza() - 10) / 2,
                        n.getRupieRilasciate());
                }
                AudioManager.getInstance().play("morte_nemico.wav");
            }
            case MORTE_BOSS_MISCHIA, MORTE_BOSS_DISTANZA -> {
                Nemico boss = livelloCorrente.getBoss();
                List<Coniglietti> nuovi = ((SpawnaConiglietti) boss).spawnaConiglietti(boss.getX(), boss.getY());
                coniglietti.addAll(nuovi);
                view.getGamePanel().setConiglietti(coniglietti);
                if (numeroLivelloCorrente <= GameConstants.LIVELLI_CON_CRISTALLO) {
                    livelloCorrente.creaCristallo(
                        boss.getX() + (boss.getLarghezza() - 8) / 2,
                        boss.getY());
                }
                AudioManager.getInstance().play("morte_boss.wav");
            }
            case GAME_OVER -> {
                statoGioco = GameState.GAME_OVER;
                profilo.aggiornaStatistiche(getNickname(), false);
                view.mostraPanel("GAME_OVER");
                AudioManager.getInstance().play("game_over.wav");
            }
            case ATTACCO_BOSS -> {
                
                if (o instanceof Barbero) AudioManager.getInstance().play("spranga.wav");
            }
            default -> { }
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
