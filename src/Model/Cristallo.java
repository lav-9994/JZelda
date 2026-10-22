package Model;

import java.awt.Rectangle;

/**
 * Cristallo lasciato a terra dai boss dei primi 5 livelli. Raccogliendoli tutti e 5 Link ottiene una spada potenziata per gli ultimi 3.
 */
public class Cristallo implements Item {

    /** Posizione x nel mondo. */
    private int x;

    /** Posizione y nel mondo. */
    private int y;

    /** Larghezza in pixel di mondo. */
    private int larghezza;

    /** Altezza in pixel di mondo. */
    private int altezza;

    /** Indica se il cristallo è stato raccolto. */
    private boolean raccolto;

    /**
     * Costruttore di Cristallo.
     *
     * @param x posizione x
     * @param y posizione y
     */
    public Cristallo(int x, int y) {
        this.x = x;
        this.y = y;
        this.larghezza = 8;
        this.altezza = 16;
        this.raccolto = false;
    }

    @Override
    public String getNome() { return "Cristallo"; }

    /**
     * Aggiunge un cristallo al conteggio di Link.
     *
     * @param link il personaggio principale
     */
    @Override
    public void applicaEffetto(Link link) {
        link.aggiungiCristallo();
    }

    /**
     * Link raccoglie il cristallo, una volta sola.
     *
     * @param link il personaggio principale
     */
    public void raccogli(Link link) {
        if (raccolto) return;
        applicaEffetto(link);
        raccolto = true;
    }

    /**
     * Rettangolo occupato dal cristallo, usato per capire se Link ci sta sopra.
     *
     * @return l'area del cristallo nel mondo
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
     * Indica se e' stato raccolto.
     *
     * @return true se e' stato raccolto
     */
    public boolean isRaccolto(){ return raccolto; }
}
