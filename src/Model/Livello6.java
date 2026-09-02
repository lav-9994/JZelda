package Model;

/**
 * Livello 6 del gioco. Boss: Morgana.
 */
public class Livello6 extends Livello {

    public Livello6() { super(6); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(6, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(6, 150, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(6, 100, 130));
        stanze.get(0).add(NemicoFactory.creaNemico(6, 200, 70));
        stanze.get(0).add(NemicoFactory.creaNemico(6, 60, 100));

        stanze.get(1).add(NemicoFactory.creaNemico(6, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(6, 160, 80));
        stanze.get(1).add(NemicoFactory.creaNemico(6, 120, 120));
        stanze.get(1).add(NemicoFactory.creaNemico(6, 60, 140));
        stanze.get(1).add(NemicoFactory.creaNemico(6, 200, 110));

        boss = new Morgana(128, 88);
    }
}
