package Model;
/**
 * Livello 3 del gioco. Boss: Vespasiano.
 */
public class Livello3 extends Livello {

    public Livello3() { super(3); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(3, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(3, 150, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(3, 100, 130));

        stanze.get(1).add(NemicoFactory.creaNemico(3, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(3, 160, 80));
        stanze.get(1).add(NemicoFactory.creaNemico(3, 120, 120));

        boss = new Vespasiano(128, 88);
    }

}
