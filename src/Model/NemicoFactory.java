package Model;
/**
 * Factory per la creazione dei minion.
 * Centralizza la politica di scalatura della difficolta': la formula che lega il numero di livello a vita e danno sta qui e 
 * non ripetuta negli otto livelli.
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

	 /**
	  * Costruttore privato: la classe espone solo il metodo statico di creazione e non va istanziata.
	  */
	 private NemicoFactory() { }

}
