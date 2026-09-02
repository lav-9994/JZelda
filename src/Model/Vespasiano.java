package Model;
/**
 * Boss a distanza del livello 3.
 * Lancia lance verso Link ogni secondo per 3 secondi,
 * poi si ferma 2 secondi prima di riprendere.
 */
public class Vespasiano extends BossDistanza{
	/** Punti vita del boss. */
    private static final int VITA = 10;
    /** Danno inflitto da ogni lancia. */
    private static final int DANNO = 2;
    /** Millisecondi tra una lancia e l'altra durante la fase di attacco. */
    private static final long INTERVALLO_RAFFICA = 1000;
    /** Durata in millisecondi della pausa tra una raffica e l'altra. */
    private static final long DURATA_PAUSA = 2000;
    /** Durata in millisecondi della fase di attacco. */
    private static final long DURATA_ATTACCO = 3000;

    /**
     * Costruttore di Vespasiano.
     *
     * @param x posizione iniziale x 
     * @param y posizione iniziale y 
     */
    public Vespasiano(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16, INTERVALLO_RAFFICA,DURATA_ATTACCO,DURATA_PAUSA);
    
    } 
    
}