package Model;
/**
 * Boss di mischia del livello 7.
 * Combatte come Link ma senza scudo.
 */

public class LinkCattivo extends BossMischia{
	/** Punti vita di LinkCattivo. */
    private static final int VITA = 30;

    /** Danno inflitto da LinkCattivo. */
    private static final int DANNO = 4;

    /** Cooldown tra un attacco e l'altro in ms. */
    private static final long COOLDOWN = 1000;
    
    /**
     * Costruttore di LinkCattivo.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    public LinkCattivo(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16, COOLDOWN);
    }

}
