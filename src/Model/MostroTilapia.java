package Model;
/**
 * Classe che rappresenta il boss MostroTilapia.
 * Lancia pesci ogni 500ms per 3 secondi, poi pausa di 4 secondi.
 * Boss a distanza del primo livello, lancia proiettili che sono pesci verso Link.
 * Alla morte spawna coniglietti.
 */
public class MostroTilapia extends BossDistanza {
	 
	/** Punti vita di Tilapia. */
    private static final int VITA = 6;
    
    /** Danno inflitto dai proiettili di Tilapia. */
    private static final int DANNO = 1;
    
    /** Intervallo tra un pesce e l'altro durante la raffica in ms. */
    private static final long INTERVALLO_RAFFICA = 500;
    
    /** Durata della fase di attacco in ms. */
    private static final long DURATA_ATTACCO = 3000;
    
    /** Durata della pausa in ms. */
    private static final long DURATA_PAUSA = 4000;
    private int frameTimer = 0;
    private boolean pinneAperte = true;
    private int yOffset = 0;
    
    

    /**
     * Costruttore di MostroTilapia.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    
    public MostroTilapia(int x, int y) {
        super(x, y, VITA, DANNO, 16, 16,INTERVALLO_RAFFICA,DURATA_ATTACCO,DURATA_PAUSA);
       
    }
    public void aggiorna() {
        frameTimer++;
        if (frameTimer >= 30) {
            frameTimer = 0;
            pinneAperte = !pinneAperte;
            yOffset = pinneAperte ? 0 : -3;
        }

 
    
    
	

}
    public int getYOffset() { return yOffset; }
}
