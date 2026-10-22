package Model;

/**
 * Livello 6 del gioco. Boss: Morgana.
 */
public class Livello6 extends Livello {

    /**
     * Costruttore di Livello6.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello6() { super(6); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(6, 96, 32));
        minion.add(NemicoFactory.creaNemico(6, 192, 144));

        minion.add(NemicoFactory.creaNemico(6, 400, 32));
        minion.add(NemicoFactory.creaNemico(6, 304, 144));

        boss = new Morgana(640, 88);
    }

}
