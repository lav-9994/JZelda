package Model;

import Config.GameConstants;

/**
 * Classe che rappresenta un proiettile sparato da un boss a distanza.
 * Si muove in una direzione fissa e si disattiva quando colpisce Link
 * o esce dai bordi della mappa.
 */

public class Proiettile {
	/** Posizione x del proiettile. */
    private int x;

    /** Posizione y del proiettile. */
    private int y;
    /** Larghezza del proiettile in pixel. */
    private static final int WIDTH = 4;

    /** Altezza del proiettile in pixel. */
    private static final int HEIGHT = 4;
   
    /** Direzione di movimento del proiettile. */
    private Direzione direzione;
    
    /** Danno inflitto a Link. */
    private int danno;
   
    
    /** Indica se il proiettile è ancora attivo. */
    private boolean attivo;
    
    
    /**
     * Costruttore di Proiettile.
     *
     * @param x         posizione iniziale x
     * @param y         posizione iniziale y
     * @param direzione direzione di movimento
     * @param danno     danno inflitto a Link
     */
    public Proiettile(int x, int y, Direzione direzione, int danno) {
        this.x = x;
        this.y = y;
        this.direzione = direzione;
        this.danno = danno;
        this.attivo = true;
        
        
    }
    
    /**
     * Muove il proiettile nella sua direzione.
     * Si disattiva se esce dai bordi della mappa.
     */
    
    public void muovi() {
        switch (direzione) {
            case SU:       y -= GameConstants.VELOCITA_PROIETTILE; break;
            case GIU:      y += GameConstants.VELOCITA_PROIETTILE; break;
            case SINISTRA: x -= GameConstants.VELOCITA_PROIETTILE; break;
            case DESTRA:   x += GameConstants.VELOCITA_PROIETTILE; break;
        }
        if (x < 0 || x > GameConstants.MAPPA_W||
            y < 0 || y > GameConstants.MAPPA_H) {
            attivo = false;
        }
    }
    /**
     * Infligge danno a Link e si disattiva.
     *
     * @param link il personaggio principale
     */
    public void infliggiDanno(Link link) {
        link.subisciDannoProiettile(danno);
        attivo = false;
    }
    
    /**
     * Disattiva il proiettile.
     */
    public void disattiva() {
        attivo = false;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDanno() { return danno; }
    public Direzione getDirezione() { return direzione; }
    public boolean isAttivo() { return attivo; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }





}
