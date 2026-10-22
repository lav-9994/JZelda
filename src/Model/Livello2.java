package Model;

/**
 * Livello 2 del gioco. Boss: Crociato.
 */
public class Livello2 extends Livello {

    /**
     * Costruttore di Livello2.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello2() { super(2); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(2, 64, 128));
        minion.add(NemicoFactory.creaNemico(2, 208, 48));

        minion.add(NemicoFactory.creaNemico(2, 352, 32));
        minion.add(NemicoFactory.creaNemico(2, 448, 128));

        boss = new Crociato(640, 88);
    }

}
