package Model;

public class Morgana extends BossDistanza {

    /** Punti vita di Morgana. */
    private static final int VITA = 24;

    /** Danno inflitto dai proiettili di Morgana. */
    private static final int DANNO = 2;

    /** Intervallo tra un proiettile e l'altro durante la raffica in ms. */
    private static final long INTERVALLO_RAFFICA = 100;
    

    /** Durata della fase di attacco in ms. */
    private static final long DURATA_ATTACCO = 2000;

    /** Durata della pausa in ms. */
    private static final long DURATA_PAUSA = 2000;
    
    /**
     * Costruttore di Morgana.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    
    public Morgana(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16, INTERVALLO_RAFFICA, DURATA_ATTACCO, DURATA_PAUSA);
    }


}
