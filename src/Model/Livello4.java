package Model;

/**
 * Livello 4 del gioco. Boss: Barbero.
 */
public class Livello4 extends Livello {

    /**
     * Costruttore di Livello4.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello4() { super(4); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(4, 80, 32));
        minion.add(NemicoFactory.creaNemico(4, 200, 112));

        minion.add(NemicoFactory.creaNemico(4, 320, 144));
        minion.add(NemicoFactory.creaNemico(4, 464, 80));

        boss = new Barbero(640, 88);
    }

}
