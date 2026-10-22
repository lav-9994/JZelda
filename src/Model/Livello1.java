package Model;

/**
 * Livello 1 del gioco. Boss: MostroTilapia.
 */
public class Livello1 extends Livello {

    /**
     * Costruttore di Livello1.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello1() { super(1); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(1, 48, 48));
        minion.add(NemicoFactory.creaNemico(1, 192, 128));

        minion.add(NemicoFactory.creaNemico(1, 304, 64));
        minion.add(NemicoFactory.creaNemico(1, 432, 112));

        boss = new MostroTilapia(640, 88);
    }

}
