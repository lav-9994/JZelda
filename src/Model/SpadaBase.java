package Model;
/**
 * Spada base di Link data all'inizio del gioco.
 */
public class SpadaBase extends Spada {
	 /**
	  * Costruttore di SpadaBase: nome fisso e danno 1.
	  */
	 public SpadaBase() {
	        super("Spada Base", 1);
	    }

	 @Override
	 public void applicaEffetto(Link link) {
	     link.equipaggiaSpada(this);
	 }
	

}
