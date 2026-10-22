package View;
import Model.*;
import Config.GameConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Pannello principale di gioco.
 * Disegna la stanza corrente, Link, nemici, boss, proiettili e coniglietti.
 * Include la barra in cima con vite, HP, rupie e carote.
 * Le sprite vengono caricate dal classpath; in caso di errore viene usato un rettangolo colorato.
 */
public class GamePanel extends JPanel {

    /** Numero di righe di un foglio di sprite: una per direzione. */
    private static final int RIGHE_FOGLIO = 4;

    /** Pixel di cui la Tilapia si solleva ondeggiando. Solo estetica. */
    private static final int GALLEGGIAMENTO = 3;

    /** Pixel percorsi da un personaggio prima di cambiare fotogramma. */
    private static final int PIXEL_PER_FOTOGRAMMA = 16;

    /**
     * Direzione a cui corrisponde ogni riga di un foglio di sprite.
     * Scritto esplicitamente invece di usare l'ordine dell'enum, così
     * riordinare Direzione non cambia il significato dei fogli gia' disegnati.
     */
    private static final Direzione[] ORDINE_RIGHE = {
        Direzione.SU, Direzione.GIU, Direzione.SINISTRA, Direzione.DESTRA
    };

    /** Riferimento a Link. */
    private Link link;

    /** Livello corrente. */
    private Livello livello;

    /** Disegna la mappa del livello corrente; sceglie il tema in base al livello. */
    private MappaView mappaView;

    /** Lista dei coniglietti attivi nella stanza. */
    private List<Coniglietti> coniglietti;

    /**
     * Fotogrammi di Link: [riga = direzione secondo ORDINE_RIGHE][colonna].
     * Le colonne sono i due passi della camminata e, per ultima, la posa di
     * attacco. Stesso formato dei fogli dei boss di mischia.
     */
    private BufferedImage[][] foglioLink;

    /**
     * Posa di Link con lo scudo alzato. Ne esiste una sola per ora, frontale.
     */
    private BufferedImage imgLinkScudo;

    /** Dimensione in pixel di mondo con cui si disegna la posa di attacco di Link. */
    private static final int DIMENSIONE_ATTACCO_LINK = 26;

    /**
     * Posa di Link che mostra il cristallo appena raccolto..
     */
    private BufferedImage imgLinkCristallo;

    /** Altezza in pixel di mondo con cui si disegna la posa col cristallo. */
    private static final int ALTEZZA_POSA_CRISTALLO = 20;

    /** Sprite del nemico base. */
    private BufferedImage imgNemico;

    /** Sprite del boss. */
    private BufferedImage imgBoss;

    /** Sprite del proiettile. */
    private BufferedImage imgProiettile;

    /** Sprite della rupia a terra. */
    private BufferedImage imgRupia;

    /** Sprite del cristallo a terra. */
    private BufferedImage imgCristallo;

    /** I due fotogrammi del mostro tilapia: pinne aperte e pinne chiuse. */
    private BufferedImage[] imgTilapia;

    /** Sprite dell'imperatore romano Vespasiano, che sta fermo e ha una posa sola. */
    private BufferedImage imgVespasiano;

    /** Sprite del mago Gandalf, che sta fermo e ha una posa sola. */
    private BufferedImage imgGandalf;

    /** Sprite della strega Morgana, che sta ferma e ha una posa sola. */
    private BufferedImage imgMorgana;

    /** Sprite di Sauron, il boss finale: sta fermo e ha una posa sola. */
    private BufferedImage imgSauron;

    /**
     * Le lance di Vespasiano, una per direzione secondo ORDINE_RIGHE. Sono disegnate piu' grandi della loro area di collisione, che resta
     * quella piccola del proiettile: si vede la lancia intera ma colpisce solo il punto in cui si trova davvero.
     */
    private BufferedImage[][] imgLancia;

    /** Dimensione in pixel di mondo con cui si disegna una lancia. */
    private static final int DIMENSIONE_LANCIA = 16;

    /**
     * I proiettili d'acqua della Tilapia, uno per direzione secondo ORDINE_RIGHE.
     * Come le lance sono disegnati piu' grandi della loro area di collisione,
     * che resta quella piccola del proiettile.
     */
    private BufferedImage[][] imgProiettileAcqua;

    /** Dimensione in pixel di mondo con cui si disegna un pesce d'acqua. */
    private static final int DIMENSIONE_PROIETTILE_ACQUA = 16;

    /**
     * I dardi magici di Gandalf, uno per direzione secondo ORDINE_RIGHE.
     * Come lance e pesci sono disegnati piu' grandi della loro area di
     * collisione, che resta quella piccola del proiettile.
     */
    private BufferedImage[][] imgProiettileMagia;

    /** Dimensione in pixel di mondo con cui si disegna un dardo magico. */
    private static final int DIMENSIONE_PROIETTILE_MAGIA = 16;

    /**
     * Le foglie di Morgana, una per direzione secondo ORDINE_RIGHE.
     * Il foglio di partenza ne aveva otto, una ogni quarantacinque gradi:
     * qui servono solo le quattro cardinali, perche' i proiettili viaggiano
     * lungo un asse alla volta.
     */
    private BufferedImage[][] imgProiettileFoglia;

    /** Dimensione in pixel di mondo con cui si disegna una foglia. */
    private static final int DIMENSIONE_PROIETTILE_FOGLIA = 16;

    /**
     * Dimensione in pixel di mondo con cui si disegna la posa di attacco di un
     * boss di mischia. E' piu' grande della sua area di collisione perche'
     * l'arma esce dal riquadro del personaggio: si vede il colpo intero, ma il
     * boss continua a colpire solo dove si trova davvero.
     */
    private static final int DIMENSIONE_ATTACCO_BOSS = 26;

    /** Fotogrammi dei coniglietti: [riga = direzione][colonna = fotogramma]. */
    private BufferedImage[][] imgConiglietto;

    /**
     * Fotogrammi dei nemici base, uno per livello.
     * Ogni elemento associa a ciascuna direzione l'immagine da disegnare.
     * I livelli senza foglio hanno una mappa vuota e ricadono su imgNemico.
     */
    private List<EnumMap<Direzione, BufferedImage>> minionPerLivello = new ArrayList<>();

    /**
     * Fogli di camminata dei boss di mischia, indicizzati per nome della classe.
     * Ogni foglio e' una griglia [riga][colonna]: la riga e' la direzione
     * secondo ORDINE_RIGHE, la colonna il fotogramma del passo.
     * I boss senza foglio ricadono su imgBoss.
     */
    private Map<String, BufferedImage[][]> foglioBossMischia = new HashMap<>();

    /** Boss di mischia del gioco, gli unici che hanno un foglio di camminata. */
    private static final String[] BOSS_DI_MISCHIA = { "Crociato", "Barbero", "LinkCattivo" };


    /** Offset orizzontale della camera in pixel di mondo (0, 256 o 512). */
    private int cameraX = 0;

    /**
     * Costruttore di GamePanel.
     * Imposta le dimensioni del pannello e carica le sprite.
     *
     * @param link il personaggio principale
     */
    public GamePanel(Link link) {
        this.link = link;
        this.coniglietti = new ArrayList<>();
        setPreferredSize(new Dimension(
        	    GameConstants.SCHERMATA_W * GameConstants.FATTORE_CONVERSIONE,         
        	    GameConstants.SCHERMATA_H * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA   
        	));
        setBackground(Color.BLACK);
        caricaSprite();
    }

    /**
     * Carica una singola immagine dal classpath.
     * In caso di errore restituisce null e stampa un messaggio di avviso.
     *
     * @param nome nome del file immagine nella cartella /sprites/
     * @return l'immagine caricata, o null se non trovata
     */
    private BufferedImage carica(String nome) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/sprites/" + nome));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Sprite non trovata: " + nome);
            return null;
        }
    }

    /**
     * Carica una sprite senza segnalare se manca.
     * Usato per le sprite ancora da disegnare.
     *
     * @param nome nome del file immagine nella cartella /sprites/
     * @return l'immagine caricata, o null se non esiste
     */
    private BufferedImage caricaSeEsiste(String nome) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/sprites/" + nome));
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Carica tutte le sprite dal classpath.
     */
    private void caricaSprite() {
        BufferedImage foglioDiLink = caricaSeEsiste("link.png");
        if (foglioDiLink != null) foglioLink = ritagliaGriglia(foglioDiLink, RIGHE_FOGLIO);
        imgLinkScudo  = caricaSeEsiste("link_scudo.png");
        imgLinkCristallo = caricaSeEsiste("link_cristallo.png");
        imgNemico     = carica("nemico.png");
        imgBoss       = carica("boss.png");
        imgProiettile = carica("proiettile.png");
        imgRupia      = carica("rupia.png");
        imgCristallo  = carica("cristallo.png");

        
        BufferedImage foglioTilapia = caricaSeEsiste("tilapia.png");
        if (foglioTilapia != null) imgTilapia = ritagliaGriglia(foglioTilapia, 1)[0];

        
        BufferedImage foglioBunny = caricaSeEsiste("bunny.png");
        if (foglioBunny != null) imgConiglietto = ritagliaGriglia(foglioBunny, 2);

       
        imgVespasiano = caricaSeEsiste("vespasiano.png");
        imgGandalf    = caricaSeEsiste("gandalf.png");
        imgMorgana    = caricaSeEsiste("morgana.png");
        imgSauron     = caricaSeEsiste("sauron.png");

       
        BufferedImage foglioLance = caricaSeEsiste("freccie.png");
        if (foglioLance != null) imgLancia = ritagliaGriglia(foglioLance, RIGHE_FOGLIO);


        BufferedImage foglioAcqua = caricaSeEsiste("proiettile_1.png");
        if (foglioAcqua != null) imgProiettileAcqua = ritagliaGriglia(foglioAcqua, RIGHE_FOGLIO);


        BufferedImage foglioMagia = caricaSeEsiste("proiettili_magia.png");
        if (foglioMagia != null) imgProiettileMagia = ritagliaGriglia(foglioMagia, RIGHE_FOGLIO);


        BufferedImage foglioFoglie = caricaSeEsiste("foglie.png");
        if (foglioFoglie != null) imgProiettileFoglia = ritagliaGriglia(foglioFoglie, RIGHE_FOGLIO);

        
        for (int livello = 1; livello <= GameConstants.NUM_LIVELLI; livello++) {
            minionPerLivello.add(ritagliaFoglio(caricaSeEsiste("minion" + livello + ".png")));
        }

        for (String nome : BOSS_DI_MISCHIA) {
            BufferedImage foglio = caricaSeEsiste(nome.toLowerCase() + ".png");
            foglioBossMischia.put(nome, foglio != null ? ritagliaGriglia(foglio, RIGHE_FOGLIO) : null);
        }
    }

    /**
     * Ritaglia un foglio di sprite nei suoi fotogrammi, uno per direzione.
     * Il foglio ha sempre quattro righe nell'ordine SU, GIU, SINISTRA, DESTRA,
     * e una colonna per ogni fotogramma di animazione: da questo il metodo
     * ricava da solo la dimensione delle celle, senza doverla sapere prima.
     * Per ora viene usata solo la prima colonna.
     *
     * @param foglio l'immagine da ritagliare, eventualmente null
     * @return i fotogrammi per direzione, vuoto se il foglio non esiste
     */
    private EnumMap<Direzione, BufferedImage> ritagliaFoglio(BufferedImage foglio) {
        EnumMap<Direzione, BufferedImage> fotogrammi = new EnumMap<>(Direzione.class);
        if (foglio == null) return fotogrammi;

        BufferedImage[][] celle = ritagliaGriglia(foglio, RIGHE_FOGLIO);
        for (int riga = 0; riga < RIGHE_FOGLIO; riga++) {
            fotogrammi.put(ORDINE_RIGHE[riga], celle[riga][0]);
        }
        return fotogrammi;
    }

    /**
     * Ritaglia un foglio di sprite in una griglia di celle quadrate.
     * Il lato della cella si ricava dall'altezza diviso il numero di righe,
     * e il numero di colonne dalla larghezza: il foglio descrive se stesso.
     *
     * @param foglio l'immagine da ritagliare
     * @param righe  quante pose contiene il foglio (4 per chi cammina, 1 per chi sta fermo)
     * @return le celle, indicizzate [riga][colonna]
     */
    private BufferedImage[][] ritagliaGriglia(BufferedImage foglio, int righe) {
        int lato = foglio.getHeight() / righe;
        int colonne = foglio.getWidth() / lato;

        BufferedImage[][] celle = new BufferedImage[righe][colonne];
        for (int r = 0; r < righe; r++) {
            for (int c = 0; c < colonne; c++) {
                celle[r][c] = foglio.getSubimage(c * lato, r * lato, lato, lato);
            }
        }
        return celle;
    }

    /**
     * Restituisce il fotogramma dei nemici del livello corrente per una direzione.
     * Se il livello non ha ancora il suo foglio, ricade sulla sprite generica.
     *
     * @param direzione la direzione verso cui il nemico si sta muovendo
     * @return l'immagine da disegnare, o null se non ce n'e' nessuna
     */
    private BufferedImage spriteMinion(Direzione direzione) {
        if (livello == null) return imgNemico;
        int indice = livello.getNumeroLivello() - 1;
        if (indice < 0 || indice >= minionPerLivello.size()) return imgNemico;

        BufferedImage fotogramma = minionPerLivello.get(indice).get(direzione);
        return fotogramma != null ? fotogramma : imgNemico;
    }

    /**
     * Disegna una sprite se disponibile, altrimenti un rettangolo colorato come fallback.
     *
     * @param g        contesto grafico
     * @param img      immagine da disegnare (può essere null)
     * @param fallback colore del rettangolo usato se l'immagine è null
     * @param x        posizione x in pixel di schermo
     * @param y        posizione y in pixel di schermo
     * @param w        larghezza in pixel di schermo
     * @param h        altezza in pixel di schermo
     */
    private void disegnaSprite(Graphics g, BufferedImage img, Color fallback,
                                int x, int y, int w, int h) {
        if (img != null) {
            g.drawImage(img, x, y, w, h, null);
        } else {
            g.setColor(fallback);
            g.fillRect(x, y, w, h);
        }
    }

    /**
     * Aggiorna il livello corrente e ridisegna.
     *
     * @param livello il livello corrente
     */
    public void setLivello(Livello livello) {
        this.livello = livello;
        this.mappaView = sceglieMappa(livello.getNumeroLivello());
        this.coniglietti = new ArrayList<>();
        repaint();
    }

    /**
     * Sceglie la MappaView giusta per il numero di livello.
     * Tutti e otto i livelli hanno la loro, quindi il ramo default non viene
     * mai raggiunto: serve solo perche' il compilatore lo pretende, e
     * restituisce comunque un oggetto valido invece di null, cosi' chi disegna
     * non deve controllare niente.
     *
     * @param numeroLivello il numero del livello, da 1 a 8
     * @return la MappaView da usare per disegnarlo
     */
    private MappaView sceglieMappa(int numeroLivello) {
        return switch (numeroLivello) {
            case 1 -> new MappaLivello1();
            case 2 -> new MappaLivello2();
            case 3 -> new MappaLivello3();
            case 4 -> new MappaLivello4();
            case 5 -> new MappaLivello5();
            case 6 -> new MappaLivello6();
            case 7 -> new MappaLivello7();
            case 8 -> new MappaLivello8();
            default -> new MappaProvvisoria();
        };
    }

    /**
     * Aggiorna la lista dei coniglietti attivi.
     *
     * @param coniglietti lista dei coniglietti da disegnare
     */
    public void setConiglietti(List<Coniglietti> coniglietti) {
        this.coniglietti = coniglietti;
    }

    /**
     * Metodo principale di rendering. Disegna la barra, la schermata ed entità.
     *
     * @param g contesto grafico
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        disegnaBarra(g);
        disegnaMappa(g);
        if (livello != null) {
            disegnaOggetti(g);
            disegnaNemici(g);
            disegnaBoss(g);
            disegnaProiettili(g);
            disegnaConiglietti(g);
        }
        disegnaLink(g);
    }
    /**
     * Imposta la posizione della camera, chiamata dal Controller a ogni
     * passo dello scorrimento fra due schermate.
     *
     * @param cameraX offset orizzontale della camera in pixel di mondo
     */
    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }

    /**
     * Disegna la barra con vite, HP, rupie e carote.
     *
     * @param g contesto grafico
     */
    private void disegnaBarra(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), GameConstants.ALTEZZA_BARRA);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.RED);
        g.drawString("Vite: " + link.getVite(), 10, 20);
        g.setColor(Color.GREEN);
        g.drawString("HP: " + link.getVita() + "/" + Link.HP_PER_VITA, 90, 20);
        g.setColor(Color.YELLOW);
        g.drawString("Rupie: " + link.getRupie(), 190, 20);
        g.setColor(Color.ORANGE);
        g.drawString("Carote: " + link.getCarote(), 300, 20);
    }

    /**
     * Disegna le tile della schermata attualmente inquadrata dalla camera,
     * delegando a MappaView il disegno vero e proprio.
     *
     * @param g contesto grafico
     */
    private void disegnaMappa(Graphics g) {
        if (livello == null) return;
        mappaView.disegna(g, livello.getMappa(), cameraX);
    }

    /**
     * Disegna Link nella posizione corrente.
     *
     * @param g contesto grafico
     */
    private void disegnaLink(Graphics g) {
        if (link.staMostrandoCristallo() && imgLinkCristallo != null) {
            disegnaSprite(g, imgLinkCristallo, Color.CYAN,
                (link.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                (link.getY() + link.getAltezza() - ALTEZZA_POSA_CRISTALLO)
                    * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                link.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
                ALTEZZA_POSA_CRISTALLO * GameConstants.FATTORE_CONVERSIONE);
            return;
        }

        if (link.isAttaccando() && foglioLink != null) {
            int margine = (DIMENSIONE_ATTACCO_LINK - link.getLarghezza()) / 2;
            disegnaSprite(g, fotogrammaLink(), Color.GREEN,
                (link.getX() - margine - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                (link.getY() - margine) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                DIMENSIONE_ATTACCO_LINK * GameConstants.FATTORE_CONVERSIONE,
                DIMENSIONE_ATTACCO_LINK * GameConstants.FATTORE_CONVERSIONE);
            return;
        }

        disegnaSprite(g, fotogrammaLink(), Color.GREEN,
            (link.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
            link.getY() * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
            link.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
            link.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
    }

    /**
     * Sceglie il fotogramma di Link.
     * Con lo scudo alzato vince sempre la posa dello scudo, perche' spada e
     * scudo non possono essere attivi insieme. Altrimenti, se sta colpendo si
     * usa l'ultima colonna, la posa di attacco; se cammina si alterna fra i
     * due passi, scelti dalla posizione come per i coniglietti: cosi'
     * l'animazione va a tempo con lo spostamento e si ferma da sola quando
     * Link e' fermo, senza bisogno di contatori nel Model.
     *
     * @return l'immagine da disegnare, o null se il foglio non e' stato caricato
     */
    private BufferedImage fotogrammaLink() {
        if (link.isScudoAttivo() && imgLinkScudo != null) return imgLinkScudo;
        if (foglioLink == null) return null;

        int riga = indiceRiga(link.getDirezione());
        int ultima = foglioLink[riga].length - 1;
        if (link.isAttaccando()) return foglioLink[riga][ultima];

        int colonneCamminata = Math.max(1, ultima);
        int colonna = Math.floorMod((link.getX() + link.getY()) / PIXEL_PER_FOTOGRAMMA,
                                    colonneCamminata);
        return foglioLink[riga][colonna];
    }

    /**
     * Disegna i nemici base vivi della stanza corrente.
     *
     * @param g contesto grafico
     */
    /**
     * Disegna le rupie e i cristalli ancora a terra.
     * Vanno disegnati per primi fra le entita', cosi' i personaggi che ci
     * passano sopra li coprono invece di finirci sotto.
     * Le liste vengono copiate prima di percorrerle: il thread del gioco
     * puo' rimuovere un oggetto raccolto mentre l'EDT sta disegnando.
     *
     * @param g contesto grafico
     */
    private void disegnaOggetti(Graphics g) {
        for (Rupia r : new ArrayList<>(livello.getRupie())) {
            if (r == null) continue;
            disegnaSprite(g, imgRupia, Color.YELLOW,
                (r.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                r.getY() * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                r.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
                r.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
        }

        for (Cristallo c : new ArrayList<>(livello.getCristalli())) {
            if (c == null) continue;
            disegnaSprite(g, imgCristallo, Color.CYAN,
                (c.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                c.getY() * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                c.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
                c.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
        }
    }

    /**
     * Disegna i nemici base vivi del livello corrente.
     *
     * @param g contesto grafico
     */
    private void disegnaNemici(Graphics g) {
        for (NemicoBase n : livello.getNemici()) {
            if (n.isVivo()) {
                disegnaSprite(g, spriteMinion(n.getDirezioneEffettiva()), Color.RED,
                    (n.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                    n.getY() * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                    n.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
                    n.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
            }
        }
    }

    /**
     * Disegna il boss del livello se è ancora vivo.
     * La Tilapia ha due fotogrammi e ondeggia leggermente: il galleggiamento
     * è calcolato qui perché sposta solo il disegno, non la sua hitbox.
     *
     * @param g contesto grafico
     */
    private void disegnaBoss(Graphics g) {
        Nemico boss = livello.getBoss();
        if (boss == null || !boss.isVivo()) return;

        BufferedImage img = imgBoss;
        int galleggiamento = 0;

        if (boss instanceof MostroTilapia tilapia && imgTilapia != null) {
            img = tilapia.isPinneAperte() ? imgTilapia[0] : imgTilapia[1];
            galleggiamento = tilapia.isPinneAperte() ? 0 : -GALLEGGIAMENTO;
        }

        if (boss instanceof Vespasiano && imgVespasiano != null) {
            img = imgVespasiano;
        }

        if (boss instanceof Gandalf && imgGandalf != null) {
            img = imgGandalf;
        }

        if (boss instanceof Morgana && imgMorgana != null) {
            img = imgMorgana;
        }

        if (boss instanceof Sauron && imgSauron != null) {
            img = imgSauron;
        }

        if (boss instanceof BossMischia bm) {
            BufferedImage fotogramma = fotogrammaBossMischia(bm);
            if (fotogramma != null) {
                img = fotogramma;
                if (bm.staAttaccando()) {
                    int margine = (DIMENSIONE_ATTACCO_BOSS - boss.getLarghezza()) / 2;
                    disegnaSprite(g, img, new Color(150, 0, 0),
                        (boss.getX() - margine - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                        (boss.getY() - margine) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                        DIMENSIONE_ATTACCO_BOSS * GameConstants.FATTORE_CONVERSIONE,
                        DIMENSIONE_ATTACCO_BOSS * GameConstants.FATTORE_CONVERSIONE);
                    return;
                }
            }
        }

        disegnaSprite(g, img, new Color(150, 0, 0),
            (boss.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
            (boss.getY() + galleggiamento) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
            boss.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
            boss.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
    }

    /**
     * Sceglie il fotogramma di camminata di un boss di mischia.
     * La riga viene dalla direzione in cui guarda, la colonna dal fotogramma
     * che il boss stesso tiene nel Model: qui non si calcola nulla.
     *
     * @param boss il boss da disegnare
     * @return l'immagine da usare, o null se il suo foglio non esiste
     */
    private BufferedImage fotogrammaBossMischia(BossMischia boss) {
        BufferedImage[][] foglio = foglioBossMischia.get(boss.getClass().getSimpleName());
        if (foglio == null) return null;

        int riga = indiceRiga(boss.getDirezione());
        // l'ultima colonna del foglio e' la posa di attacco, le precedenti
        // sono il ciclo di camminata
        int ultima = foglio[riga].length - 1;
        int colonneCamminata = Math.max(1, ultima);
        int colonna = boss.staAttaccando()
                ? ultima
                : boss.getFotogramma() % colonneCamminata;
        return foglio[riga][colonna];
    }

    /**
     * Restituisce la riga del foglio che corrisponde a una direzione,
     * secondo la convenzione ORDINE_RIGHE.
     *
     * @param direzione la direzione cercata
     * @return l'indice della riga
     */
    private int indiceRiga(Direzione direzione) {
        for (int i = 0; i < ORDINE_RIGHE.length; i++) {
            if (ORDINE_RIGHE[i] == direzione) return i;
        }
        return 0;
    }

    /**
     * Disegna i proiettili attivi del boss a distanza.
     * La lista viene copiata prima di percorrerla: il thread del gioco
     * puo' rimuovere un proiettile spento mentre l'EDT sta disegnando.
     *
     * @param g contesto grafico
     */
    private void disegnaProiettili(Graphics g) {

        if (!(livello.getBoss() instanceof BossDistanza)) return;
        BossDistanza boss = (BossDistanza) livello.getBoss();
        boolean lance = boss instanceof Vespasiano && imgLancia != null;
        boolean acqua = boss instanceof MostroTilapia && imgProiettileAcqua != null;
        // Sauron scaglia gli stessi dardi magici di Gandalf
        boolean magia = (boss instanceof Gandalf || boss instanceof Sauron)
                        && imgProiettileMagia != null;
        boolean foglie = boss instanceof Morgana && imgProiettileFoglia != null;

        for (Proiettile p : new ArrayList<>(boss.getProiettili())) {
            if (p == null || !p.isAttivo()) continue;

            if (lance) {

                int margine = (DIMENSIONE_LANCIA - p.getLarghezza()) / 2;
                disegnaSprite(g, imgLancia[indiceRiga(p.getDirezione())][0], Color.YELLOW,
                    (p.getX() - margine - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                    (p.getY() - margine) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                    DIMENSIONE_LANCIA * GameConstants.FATTORE_CONVERSIONE,
                    DIMENSIONE_LANCIA * GameConstants.FATTORE_CONVERSIONE);
            } else if (acqua) {

                int margine = (DIMENSIONE_PROIETTILE_ACQUA - p.getLarghezza()) / 2;
                disegnaSprite(g, imgProiettileAcqua[indiceRiga(p.getDirezione())][0], Color.CYAN,
                    (p.getX() - margine - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                    (p.getY() - margine) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                    DIMENSIONE_PROIETTILE_ACQUA * GameConstants.FATTORE_CONVERSIONE,
                    DIMENSIONE_PROIETTILE_ACQUA * GameConstants.FATTORE_CONVERSIONE);
            } else if (magia) {

                int margine = (DIMENSIONE_PROIETTILE_MAGIA - p.getLarghezza()) / 2;
                disegnaSprite(g, imgProiettileMagia[indiceRiga(p.getDirezione())][0], Color.MAGENTA,
                    (p.getX() - margine - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                    (p.getY() - margine) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                    DIMENSIONE_PROIETTILE_MAGIA * GameConstants.FATTORE_CONVERSIONE,
                    DIMENSIONE_PROIETTILE_MAGIA * GameConstants.FATTORE_CONVERSIONE);
            } else if (foglie) {

                int margine = (DIMENSIONE_PROIETTILE_FOGLIA - p.getLarghezza()) / 2;
                disegnaSprite(g, imgProiettileFoglia[indiceRiga(p.getDirezione())][0], Color.GREEN,
                    (p.getX() - margine - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                    (p.getY() - margine) * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                    DIMENSIONE_PROIETTILE_FOGLIA * GameConstants.FATTORE_CONVERSIONE,
                    DIMENSIONE_PROIETTILE_FOGLIA * GameConstants.FATTORE_CONVERSIONE);
            } else {
                disegnaSprite(g, imgProiettile, Color.YELLOW,
                    (p.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                    p.getY() * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                    p.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
                    p.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
            }
        }
    }

    /**
     * Disegna i coniglietti ancora attivi.
     * La lista viene copiata prima di percorrerla: il thread del gioco
     * rimuove i coniglietti che escono dall'arena mentre l'EDT sta
     * disegnando, e percorrerla direttamente lanciava
     * ConcurrentModificationException.
     *
     * @param g contesto grafico
     */
    private void disegnaConiglietti(Graphics g) {
        for (Coniglietti c : new ArrayList<>(coniglietti)) {
            if (c == null || !c.isAttivo()) continue;
            disegnaSprite(g, fotogrammaConiglietto(c), Color.PINK,
                (c.getX() - cameraX) * GameConstants.FATTORE_CONVERSIONE,
                c.getY() * GameConstants.FATTORE_CONVERSIONE + GameConstants.ALTEZZA_BARRA,
                c.getLarghezza() * GameConstants.FATTORE_CONVERSIONE,
                c.getAltezza() * GameConstants.FATTORE_CONVERSIONE);
        }
    }

    /**
     * Sceglie il fotogramma di un coniglietto.
     * La posa viene dalla direzione, il fotogramma dell'animazione da quanto
     * il coniglietto si e' spostato: cosi' le zampe si muovono perche' avanza,
     * e non serve nessun contatore da tenere aggiornato.
     *
     * @param c il coniglietto da disegnare
     * @return l'immagine da usare, o null se il foglio non e' stato caricato
     */
    private BufferedImage fotogrammaConiglietto(Coniglietti c) {
        if (imgConiglietto == null) return null;
        int riga = (c.getDirezione() == Direzione.SINISTRA) ? 0 : 1;
        int colonna = Math.floorMod(c.getX() / PIXEL_PER_FOTOGRAMMA,
                                    imgConiglietto[riga].length);
        return imgConiglietto[riga][colonna];
    }
}