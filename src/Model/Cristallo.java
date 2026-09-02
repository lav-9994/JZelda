package Model;
/**
 * Classe che rappresenta un cristallo droppato da un boss.
 * Link deve raccogliere 5 cristalli per completare i primi 5 dungeon.
 */
public class Cristallo {
	 /** Posizione x nella stanza. */
    private int x;
    
    /** Posizione y nella stanza. */
    private int y;
    
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
        this.raccolto = false;
    }
    
    /**
     * Link raccoglie il cristallo.
     *
     * @param link il personaggio principale
     */
    
    public void raccogli(Link link) {
        if (!raccolto) {
            link.aggiungiCristallo();
            raccolto = true;
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isRaccolto() { return raccolto; }

}
