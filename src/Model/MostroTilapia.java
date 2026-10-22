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
    /** Frame trascorsi dall'ultimo cambio di posa. */
    private int frameTimer = 0;

    /** Posa corrente: true con le pinne aperte, false con le pinne chiuse. */
    private boolean pinneAperte = true;

    /** Frame che durano una posa prima di passare all'altra. */
    private static final int FRAME_PER_POSA = 30;
    
    

    /**
     * Costruttore di MostroTilapia.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    
    public MostroTilapia(int x, int y) {
        super(x, y, VITA, DANNO, 24, 24, INTERVALLO_RAFFICA, DURATA_ATTACCO, DURATA_PAUSA);
       
    }
    /**
     * Fa avanzare l'animazione delle pinne.
     * Va chiamato una volta per frame dal game loop.
     */
    public void aggiorna() {
        frameTimer++;
        if (frameTimer >= FRAME_PER_POSA) {
            frameTimer = 0;
            pinneAperte = !pinneAperte;
        }
    }

    /**
     * Indica se in questo momento la Tilapia ha le pinne aperte.
     *
     * @return true se in questo momento la Tilapia ha le pinne aperte
     */
    public boolean isPinneAperte() { return pinneAperte; }
}
