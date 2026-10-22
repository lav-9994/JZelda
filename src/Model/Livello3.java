package Model;

/**
 * Livello 3 del gioco. Boss: Vespasiano.
 */
public class Livello3 extends Livello {

    /**
     * Costruttore di Livello3.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello3() { super(3); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(3, 32, 96));
        minion.add(NemicoFactory.creaNemico(3, 176, 144));

        minion.add(NemicoFactory.creaNemico(3, 288, 112));
        minion.add(NemicoFactory.creaNemico(3, 416, 48));

        boss = new Vespasiano(640, 88);
    }

}
