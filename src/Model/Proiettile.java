package Model;

import Config.GameConstants;

/**
 * Classe che rappresenta un proiettile sparato da un boss a distanza. Si muove in una direzione fissa, verso Link (meccanica presente 
 * in BossDistanza non qui)  e si disattiva quando lo colpisce o esce dai bordi della mappa.
 */

public class Proiettile {
	/** Posizione x del proiettile. */
    private int x;

    /** Posizione y del proiettile. */
    private int y;
    /** Larghezza del proiettile in pixel di mondo. */
    private static final int LARGHEZZA = 4;

    /** Altezza del proiettile in pixel di mondo. */
    private static final int ALTEZZA = 4;
   
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
    
    /**
     * Restituisce la posizione x.
     *
     * @return la posizione x
     */
    public int getX() { return x; }
    /**
     * Restituisce la posizione y.
     *
     * @return la posizione y
     */
    public int getY() { return y; }
    /**
     * Restituisce il danno inflitto.
     *
     * @return il danno inflitto
     */
    public int getDanno() { return danno; }
    /**
     * Restituisce la direzione verso cui e' rivolto.
     *
     * @return la direzione verso cui e' rivolto
     */
    public Direzione getDirezione() { return direzione; }
    /**
     * Indica se e' ancora attivo.
     *
     * @return true se e' ancora attivo
     */
    public boolean isAttivo() { return attivo; }
    /**
     * Restituisce la larghezza in pixel di mondo.
     *
     * @return la larghezza in pixel di mondo
     */
    public int getLarghezza() { return LARGHEZZA; }
    /**
     * Restituisce l'altezza in pixel di mondo.
     *
     * @return l'altezza in pixel di mondo
     */
    public int getAltezza() { return ALTEZZA; }





}
