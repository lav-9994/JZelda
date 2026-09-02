package View;
import Model.*;
import Config.GameConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Pannello principale di gioco.
 * Disegna la stanza corrente, Link, nemici, boss, proiettili e coniglietti.
 * Include l'HUD in cima con vite, HP, rupie e carote.
 * Le sprite vengono caricate dal classpath; in caso di errore viene usato un rettangolo colorato.
 */
public class GamePanel extends JPanel {

    /** Fattore di scala per rendere la finestra più grande. */
    private static final int SCALE = 3;

    /** Altezza in pixel dell'HUD. */
    private static final int HUD_HEIGHT = 32;

    /** Riferimento a Link. */
    private Link link;

    /** Livello corrente. */
    private Livello livello;

    /** Lista dei coniglietti attivi nella stanza. */
    private List<Coniglietti> coniglietti;

    /** Sprite di Link. */
    private BufferedImage imgLink;

    /** Sprite del nemico base. */
    private BufferedImage imgNemico;

    /** Sprite del boss. */
    private BufferedImage imgBoss;

    /** Sprite del proiettile. */
    private BufferedImage imgProiettile;


    /** Offset orizzontale della camera in pixel (0, 256 o 512). */
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
        	    GameConstants.SCHERMATA_W * SCALE,         
        	    GameConstants.SCHERMATA_H * SCALE + HUD_HEIGHT   
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
     * Carica tutte le sprite dal classpath.
     */
    private void caricaSprite() {
        imgLink       = carica("link.png");
        imgNemico     = carica("nemico.png");
        imgBoss       = carica("boss.png");
        imgProiettile = carica("proiettile.png");
       
    }

    /**
     * Disegna una sprite se disponibile, altrimenti un rettangolo colorato come fallback.
     *
     * @param g        contesto grafico
     * @param img      immagine da disegnare (può essere null)
     * @param fallback colore del rettangolo usato se l'immagine è null
     * @param x        posizione x in pixel
     * @param y        posizione y in pixel
     * @param w        larghezza in pixel
     * @param h        altezza in pixel
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
        this.coniglietti = new ArrayList<>();
        repaint();
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
     * Metodo principale di rendering. Disegna HUD, stanza ed entità.
     *
     * @param g contesto grafico
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        disegnaHUD(g);
        disegnaMappa(g);
        if (livello != null) {
            disegnaNemici(g);
            disegnaBoss(g);
            disegnaProiettili(g);
            disegnaConiglietti(g);
        }
        disegnaLink(g);
    }
    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }

    /**
     * Disegna l'HUD con vite, HP, rupie e carote.
     *
     * @param g contesto grafico
     */
    private void disegnaHUD(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), HUD_HEIGHT);
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
     * Disegna Link nella posizione corrente.
     *
     * @param g contesto grafico
     */
    private void disegnaLink(Graphics g) {
        disegnaSprite(g, imgLink, Color.GREEN,
            link.getX() * SCALE,
            link.getY() * SCALE + HUD_HEIGHT,
            link.getWidth() * SCALE,
            link.getHeight() * SCALE);
    }

    /**
     * Disegna i nemici base vivi della stanza corrente.
     *
     * @param g contesto grafico
     */
    private void disegnaNemici(Graphics g) {
        for (NemicoBase n : livello.getNemici()) {
            if (n.isVivo()) {
                disegnaSprite(g, imgNemico, Color.RED,
                    n.getX() * SCALE,
                    n.getY() * SCALE + HUD_HEIGHT,
                    n.getWidth() * SCALE,
                    n.getHeight() * SCALE);
            }
        }
    }

    /**
     * Disegna il boss nella terza stanza se è ancora vivo.
     *
     * @param g contesto grafico
     */
    private void disegnaBoss(Graphics g) {
        
        Nemico boss = livello.getBoss();
        if (boss == null || !boss.isVivo()) return;
        disegnaSprite(g, imgBoss, new Color(150, 0, 0),
            boss.getX() * SCALE,
            boss.getY() * SCALE + HUD_HEIGHT,
            boss.getWidth() * SCALE,
            boss.getHeight() * SCALE);
    }

    /**
     * Disegna i proiettili attivi del boss a distanza.
     *
     * @param g contesto grafico
     */
    private void disegnaProiettili(Graphics g) {
        
        if (!(livello.getBoss() instanceof BossDistanza)) return;
        BossDistanza boss = (BossDistanza) livello.getBoss();
        for (Proiettile p : boss.getProiettili()) {
            if (p.isAttivo()) {
                disegnaSprite(g, imgProiettile, Color.YELLOW,
                    p.getX() * SCALE,
                    p.getY() * SCALE + HUD_HEIGHT,
                    4 * SCALE,
                    4 * SCALE);
            }
        }
    }

    /**
     * Disegna i coniglietti attivi in rosa.
     * Non hanno sprite dedicata.
     *
     * @param g contesto grafico
     */
    private void disegnaConiglietti(Graphics g) {
        g.setColor(Color.PINK);
        for (Coniglietti c : coniglietti) {
            if (c.isAttivo()) {
                g.fillRect(
                    c.getX() * SCALE,
                    c.getY() * SCALE + HUD_HEIGHT,
                    c.getWidth() * SCALE,
                    c.getHeight() * SCALE);
            }
        }
    }

    /** @return il fattore di scala */
    public int getScale() { return SCALE; }

    /** @return l'altezza dell'HUD in pixel */
    public int getHudHeight() { return HUD_HEIGHT; }
}