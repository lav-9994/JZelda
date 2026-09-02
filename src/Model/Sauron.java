package Model;
/**
 * Boss a distanza del livello 8. Boss finale del gioco.
 * Spara un proiettile ogni secondo senza mai fermarsi.
 */
public class Sauron extends BossDistanza{
	
	/** Punti vita di Sauron. */
    private static final int VITA = 35;

    /** Danno inflitto dai proiettili di Sauron. */
    private static final int DANNO = 4;

    /** Intervallo tra un proiettile e l'altro in ms. */
    private static final long INTERVALLO_RAFFICA = 1000;
    
    /**
     * Costruttore di Sauron.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    
    public Sauron(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16, INTERVALLO_RAFFICA, Long.MAX_VALUE, 0);
    }


}
