package Model;
/**
 * Classe che rappresenta una rupia fisica nella stanza.
 * Appare quando un nemico muore e viene raccolta da Link passandoci sopra.
 */

public class Rupia {
	/** Posizione x nella stanza. */
    private int x;
   
    /** Posizione y nella stanza. */
    private int y;
    
    /** Valore in rupie (da 1 a 3). */
    private int valore;
    
    /** Indica se la rupia è stata raccolta. */
    private boolean raccolta;
    
    /**
     * Costruttore di Rupia.
     *
     * @param x      posizione x
     * @param y      posizione y
     * @param valore valore della rupia
     */
    
    public Rupia(int x, int y, int valore) {
        this.x = x;
        this.y = y;
        this.valore = valore;
        this.raccolta = false;
    }
    /**
     * Se la moneta non è stata raccolta, viene raccolta e aggiunta alle rupie
     *
     * @param link il personaggio principale
     */
    public void raccogli(Link link) {
        if (!raccolta) {
            link.aggiungiRupie(valore);
            raccolta = true;
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getValore() { return valore; }
    public boolean isRaccolta() { return raccolta; }

}
