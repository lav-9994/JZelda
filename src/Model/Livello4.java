package Model;
/**
 * Livello 4 del gioco. Boss: Barbero.
 */
public class Livello4 extends Livello {

    public Livello4() { super(4); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(4, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(4, 150, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(4, 100, 130));
        stanze.get(0).add(NemicoFactory.creaNemico(4, 200, 70));

        stanze.get(1).add(NemicoFactory.creaNemico(4, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(4, 160, 80));
        stanze.get(1).add(NemicoFactory.creaNemico(4, 120, 120));
        stanze.get(1).add(NemicoFactory.creaNemico(4, 60, 140));

        boss = new Barbero(128, 88);
    }
}
