package Model;
/**
 * Carota acquistabile nello shop.
 * Può essere usata per recuperare vita e hanno un'interazione con i conigli.
 */

public class Carote implements Item {
	 @Override
	    public String getNome() { return "Carota"; }

	    @Override
	    public void applicaEffetto(Link link) {
	        link.aggiungiCarote(1);
	    }

}
