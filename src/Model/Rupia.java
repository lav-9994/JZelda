package Model;

import java.awt.Rectangle;

/**
 * Rupia lasciata a terra da un nemico alla sua morte.
 * Viene raccolta da Link passandoci sopra.
 */
public class Rupia implements Item {

    /** Posizione x nel mondo. */
    private int x;

    /** Posizione y nel mondo. */
    private int y;

    /** Larghezza in pixel di mondo. */
    private int larghezza;

    /** Altezza in pixel di mondo. */
    private int altezza;

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
        this.larghezza = 10;
        this.altezza = 10;
        this.valore = valore;
        this.raccolta = false;
    }

    @Override
    public String getNome() { return "Rupia"; }

    /**
     * Aggiunge il valore della rupia al contatore di Link.
     *
     * @param link il personaggio principale
     */
    @Override
    public void applicaEffetto(Link link) {
        link.aggiungiRupie(valore);
    }

    /**
     * Link raccoglie la rupia, una volta sola.
     *
     * @param link il personaggio principale
     */
    public void raccogli(Link link) {
        if (raccolta) return;
        applicaEffetto(link);
        raccolta = true;
    }

    /**
     * Rettangolo occupato dalla rupia, usato per capire se Link ci sta sopra.
     *
     * @return l'area della rupia nel mondo
     */
    public Rectangle getArea() {
        return new Rectangle(x, y, larghezza, altezza);
    }

    /**
     * Restituisce la posizione x.
     *
     * @return la posizione x
     */
    public int getX()          { return x; }
    /**
     * Restituisce la posizione y.
     *
     * @return la posizione y
     */
    public int getY()          { return y; }
    /**
     * Restituisce la larghezza in pixel di mondo.
     *
     * @return la larghezza in pixel di mondo
     */
    public int getLarghezza()  { return larghezza; }
    /**
     * Restituisce l'altezza in pixel di mondo.
     *
     * @return l'altezza in pixel di mondo
     */
    public int getAltezza()    { return altezza; }
    /**
     * Restituisce il valore in rupie.
     *
     * @return il valore in rupie
     */
    public int getValore()     { return valore; }
    /**
     * Indica se e' stata raccolta.
     *
     * @return true se e' stata raccolta
     */
    public boolean isRaccolta(){ return raccolta; }
}
