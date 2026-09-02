package Model;
import java.util.Observable;
import java.awt.Rectangle;
/**
 * Classe astratta che rappresenta un personaggio del gioco.
 * È la classe padre di tutte le entità che hanno una posizione,
 * vita e possono subire danni.
 * Estende Observable per notificare la View dei cambiamenti di stato.
 */

public abstract class Personaggio extends Observable {
	 /** Posizione x del personaggio nella mappa. */
    protected int x;

    /** Posizione y del personaggio nella mappa. */
    protected int y;
    protected int prevX, prevY;
    protected Rectangle solidArea;


    /** Punti vita del personaggio. */
    protected int vita;

    /** Danno inflitto dal personaggio. */
    protected int danno;

    /** Larghezza del personaggio in pixel. */
    protected int width;

    /** Altezza del personaggio in pixel. */
    protected int height;

    /** Direzione corrente del personaggio. */
    protected Direzione direzione;

    /**
     * Costruttore della classe Personaggio.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     * @param vita punti vita iniziali
     * @param danno danno inflitto
     * @param width larghezza in pixel
     * @param height altezza in pixel
     */
    public Personaggio(int x, int y, int vita, int danno, int width, int height) {
        this.x = x;
        this.y = y;
        this.vita = vita;
        this.danno = danno;
        this.width = width;
        this.height = height;
        this.solidArea = new Rectangle(0, 0, width, height);
        this.direzione = Direzione.GIU;
    }

    /**
     * Muove il personaggio. Implementazione definita dalle sottoclassi.
     */
    public abstract void muovi();

    /**
     * Infligge danni al personaggio riducendone la vita.
     * Notifica gli observer se la vita cambia.
     *
     * @param danno quantità di danno subito
     */
    public void subisciDanno(int danno) {
        this.vita -= danno;
        if (this.vita < 0) this.vita = 0;
        setChanged();
        notifyObservers("danno");
    }
     

    /**
     * Controlla se il personaggio è ancora vivo.
     *
     * @return true se la vita è maggiore di zero
     */
    public boolean isVivo() {
        return vita > 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getVita() { return vita; }
    public int getDanno() { return danno; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Direzione getDirezione() { return direzione; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDirezione(Direzione direzione) { this.direzione = direzione; }
    public Rectangle getSolidArea() { return solidArea; }

}
