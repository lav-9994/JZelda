package Model;

/**
 * Livello 8 del gioco. Boss finale: Sauron.
 */
public class Livello8 extends Livello {

    public Livello8() { super(8); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(8, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(8, 150, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(8, 100, 130));
        stanze.get(0).add(NemicoFactory.creaNemico(8, 200, 70));
        stanze.get(0).add(NemicoFactory.creaNemico(8, 60, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(8, 180, 140));

        stanze.get(1).add(NemicoFactory.creaNemico(8, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(8, 160, 80));
        stanze.get(1).add(NemicoFactory.creaNemico(8, 120, 120));
        stanze.get(1).add(NemicoFactory.creaNemico(8, 60, 140));
        stanze.get(1).add(NemicoFactory.creaNemico(8, 200, 110));
        stanze.get(1).add(NemicoFactory.creaNemico(8, 130, 50));

        boss = new Sauron(128, 88);
    }

}
