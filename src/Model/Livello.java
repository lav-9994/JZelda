package Model;
import java.util.ArrayList;
import Config.GameConstants;
import java.util.List;
/**
 * Classe astratta che rappresenta un livello del gioco.
 * Ogni livello ha un boss e una lista di nemici base creati tramite NemicoFactory.
 */
public abstract class Livello {
	
	/** Numero del livello (1-8). */
    protected int numeroLivello;
   

    /** Mappa del livello: matrice di tile [righe][colonne]. */
    protected int[][] mappa = new int[GameConstants.MAPPA_TILE_H][GameConstants.MAPPA_TILE_W];

    /** Lista di tutti i nemici del livello. */
    protected List<NemicoBase> minion;
 
    
    /** Boss del livello. */
    protected Nemico boss;
    
    
    /**
     * Costruttore di Livello.
     *
     * @param numeroLivello numero del livello corrente
     */
    public Livello(int numeroLivello) {
        this.numeroLivello = numeroLivello;
        this.minion = new ArrayList<>();
      
    }
    

    /**
     * Controlla se il livello è completato (boss sconfitto).
     *
     * @return true se il livello è completato
     */
    public boolean isCompletato() {
        return  boss != null && !boss.isVivo();
    }

    public int getNumeroLivello() { return numeroLivello; }
  
   
    public Nemico getBoss() { return boss; }
    public int[][] getMappa()        { return mappa; }
    public List<NemicoBase> getNemici() { return minion; }
}
        
        
    
  


