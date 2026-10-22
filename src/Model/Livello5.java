package Model;

/**
 * Livello 5 del gioco. Boss: Gandalf.
 */
public class Livello5 extends Livello {

    /**
     * Costruttore di Livello5.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello5() { super(5); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(5, 48, 144));
        minion.add(NemicoFactory.creaNemico(5, 176, 64));

        minion.add(NemicoFactory.creaNemico(5, 368, 64));
        minion.add(NemicoFactory.creaNemico(5, 288, 128));

        boss = new Gandalf(640, 88);
    }

}
