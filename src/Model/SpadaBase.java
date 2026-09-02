package Model;
/**
 * Spada base di Link data all'inizio del gioco.
 */
public class SpadaBase extends Spada {
	 public SpadaBase() {
	        super("Spada Base", 1);
	    }

	 @Override
	 public void applicaEffetto(Link link) {
	     link.equipaggiaSpada(this);
	 }
	

}
