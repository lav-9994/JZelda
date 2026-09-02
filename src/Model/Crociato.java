package Model;
/**
 * Boss di mischia del livello 2.
 * Si avvicina a Link e attacca corpo a corpo.
 */

public class Crociato extends BossMischia {
	private static final int VITA = 12;
    private static final int DANNO = 2;
    private static final long COOLDOWN = 1500;
    
    /**
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    public Crociato(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16, COOLDOWN);
    }


}
