package Model;
/**
 * Boss a distanza del livello 5.
 * Lancia proiettili ogni 200ms per 2 secondi,
 * poi pausa di 2 secondi.
 */
public class Gandalf extends BossDistanza{
	/** Punti vita di Gandalf. */
    private static final int VITA = 24;

    /** Danno inflitto dai proiettili di Gandalf. */
    private static final int DANNO = 3;
    
    /** Intervallo tra un proiettile e l'altro durante la raffica in ms. */
    private static final long INTERVALLO_RAFFICA = 200;
    
    /** Durata della fase di attacco in ms. */
    private static final long DURATA_ATTACCO = 2000;

    /** Durata della pausa in ms. */
    private static final long DURATA_PAUSA = 2000;
    
    
    /**
     * Costruttore di Gandalf.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    public Gandalf(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16, INTERVALLO_RAFFICA,DURATA_ATTACCO,DURATA_PAUSA);
      
    }
}
    
  