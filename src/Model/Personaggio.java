package Model;
import java.util.Observable;
/**
 * Classe astratta che rappresenta un personaggio del gioco.
 * È la classe padre di tutte le entità che hanno una posizione, vita e possono subire danni.
 * Estende Observable per notificare la View dei cambiamenti di stato, così tutte le entità che estendono Personaggio estendono 
 * anche Observable e notificano i cambiamenti di stato.
 */

public abstract class Personaggio extends Observable {
	 /** Posizione x del personaggio nella mappa. */
    protected int x;

    /** Posizione y del personaggio nella mappa. */
    protected int y;

    /** Punti vita del personaggio. */
    protected int vita;

    /** Danno inflitto dal personaggio. */
    protected int danno;

    /** Larghezza del personaggio in pixel di mondo. */
    protected int larghezza;

    /** Altezza del personaggio in pixel di mondo. */
    protected int altezza;

    /** Direzione corrente del personaggio. */
    protected Direzione direzione;

    /**
     * Costruttore della classe Personaggio.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     * @param vita punti vita iniziali
     * @param danno danno inflitto
     * @param larghezza larghezza in pixel di mondo
     * @param altezza altezza in pixel di mondo
     */
    public Personaggio(int x, int y, int vita, int danno, int larghezza, int altezza) {
        this.x = x;
        this.y = y;
        this.vita = vita;
        this.danno = danno;
        this.larghezza = larghezza;
        this.altezza = altezza;
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
        notifyObservers(Evento.DANNO_SUBITO);
    }
     

    /**
     * Controlla se il personaggio è ancora vivo.
     *
     * @return true se la vita è maggiore di zero
     */
    public boolean isVivo() {
        return vita > 0;
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
     * Restituisce i punti vita attuali.
     *
     * @return i punti vita attuali
     */
    public int getVita() { return vita; }
    /**
     * Restituisce il danno inflitto.
     *
     * @return il danno inflitto
     */
    public int getDanno() { return danno; }
    /**
     * Restituisce la larghezza in pixel di mondo.
     *
     * @return la larghezza in pixel di mondo
     */
    public int getLarghezza() { return larghezza; }
    /**
     * Restituisce l'altezza in pixel di mondo.
     *
     * @return l'altezza in pixel di mondo
     */
    public int getAltezza() { return altezza; }
    /**
     * Restituisce la direzione verso cui e' rivolto.
     *
     * @return la direzione verso cui e' rivolto
     */
    public Direzione getDirezione() { return direzione; }
    /**
     * Imposta la posizione x.
     *
     * @param x il nuovo valore
     */
    public void setX(int x) { this.x = x; }
    /**
     * Imposta la posizione y.
     *
     * @param y il nuovo valore
     */
    public void setY(int y) { this.y = y; }
    /**
     * Imposta la direzione verso cui e' rivolto.
     *
     * @param direzione il nuovo valore
     */
    public void setDirezione(Direzione direzione) { this.direzione = direzione; }

}
