package Model;
/**
 * Carota acquistabile nello shop. Sono l'equivalente delle pozioni nei giochi originali e mangiandone una viene ripristinata della vita.
 * Ancora da realizzare nella view l'animazione di Link che mangia la carota. I coniglietti hanno un'interazione speciale con Link in base alla 
 * presenza o meno della carota in inventario.
 */

public class Carote implements Item {
	 @Override
	    public String getNome() { return "Carota"; }

	    @Override
	    public void applicaEffetto(Link link) {
	        link.aggiungiCarote(1);
	    }

}
