package Model;
/**
 * Spada potenziata di Link, ottenuta dopo il livello 5.
 * Infligge danno doppio rispetto alla spada normale.
 */
public class SpadaFinale extends Spada {
	public SpadaFinale() {
        super("Spada Finale", 2);
    }
	@Override
	public void applicaEffetto(Link link) {
	    link.equipaggiaSpada(this);
	}
}
