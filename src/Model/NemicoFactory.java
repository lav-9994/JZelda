package Model;
/**
 * Factory per la creazione dei nemici base.
 * Scala vita e danno in base al livello corrente.
 * Applica il pattern Factory Method centralizzando la creazione dei nemici.
 */
public class NemicoFactory {
	/**
     * Crea un NemicoBase con stats scalate in base al livello.
     *
     * @param livello numero del livello corrente (1-8)
     * @param x       posizione iniziale x
     * @param y       posizione iniziale y
     * @return un NemicoBase configurato per il livello
     */
	 public static NemicoBase creaNemico(int livello, int x, int y) {
	        int vita = 2 + livello;
	        int danno = 1 + (livello / 3);
	        return new NemicoBase(x, y, vita, danno);
	    }

}
