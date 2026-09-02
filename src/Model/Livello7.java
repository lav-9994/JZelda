package Model;

/**
 * Livello 7 del gioco. Boss: LinkCattivo.
 */
public class Livello7 extends Livello {

    public Livello7() { super(7); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(7, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(7, 150, 100));
        stanze.get(0).add(NemicoFactory.creaNemico(7, 100, 130));
        stanze.get(0).add(NemicoFactory.creaNemico(7, 200, 70));
        stanze.get(0).add(NemicoFactory.creaNemico(7, 60, 100));

        stanze.get(1).add(NemicoFactory.creaNemico(7, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(7, 160, 80));
        stanze.get(1).add(NemicoFactory.creaNemico(7, 120, 120));
        stanze.get(1).add(NemicoFactory.creaNemico(7, 60, 140));
        stanze.get(1).add(NemicoFactory.creaNemico(7, 200, 110));

        boss = new LinkCattivo(128, 88);
    }

}
