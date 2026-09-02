package Model;
/**
 * Livello 2 del gioco. Boss: Crociato.
 */
public class Livello2 extends Livello {

    public Livello2() { super(2); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(2, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(2, 150, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(2, 100, 130));

        stanze.get(1).add(NemicoFactory.creaNemico(2, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(2, 160, 80));
        stanze.get(1).add(NemicoFactory.creaNemico(2, 120, 120));

        boss = new Crociato(128, 88);
    }

}
