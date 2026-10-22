package Model;

/**
 * Livello 7 del gioco. Boss: LinkCattivo.
 */
public class Livello7 extends Livello {

    /**
     * Costruttore di Livello7.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello7() { super(7); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(7, 64, 64));
        minion.add(NemicoFactory.creaNemico(7, 208, 128));

        minion.add(NemicoFactory.creaNemico(7, 336, 96));
        minion.add(NemicoFactory.creaNemico(7, 464, 32));

        boss = new LinkCattivo(640, 88);
    }

}
