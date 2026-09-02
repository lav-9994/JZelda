package Model;
/**
 * Classe che rappresenta il boss Barbero.
 * Combatte in mischia con una spranga.
 * Alla morte spawna coniglietti.
 */
public class Barbero extends BossMischia{
	
	/** Punti vita di Barbero. */
    private static final int VITA = 20;
    
    /** Danno inflitto da Barbero. */
    private static final int DANNO = 3;
    
    /** Cooldown tra un attacco e l'altro in ms. */
    private static final long COOLDOWN = 1500;

    
    /**
     * Costruttore di Barbero.
     *
     * @param x    posizione iniziale x
     * @param y    posizione iniziale y
     */
	public Barbero(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16,COOLDOWN);
    }

}
