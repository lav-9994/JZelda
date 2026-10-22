package Model;

/**
 * Livello 8 del gioco. Boss finale: Sauron.
 */
public class Livello8 extends Livello {

    /**
     * Costruttore di Livello8.
     * Il costruttore della superclasse carica la mappa e chiama inizializza().
     */
    public Livello8() { super(8); }

    @Override
    protected void inizializza() {
        minion.add(NemicoFactory.creaNemico(8, 32, 128));
        minion.add(NemicoFactory.creaNemico(8, 192, 32));

        minion.add(NemicoFactory.creaNemico(8, 288, 48));
        minion.add(NemicoFactory.creaNemico(8, 432, 144));

        boss = new Sauron(640, 88);
    }

}
