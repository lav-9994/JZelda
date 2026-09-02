package Model;
/**
 * Livello 1 del gioco. Boss: MostroTilapia.
 */
public class Livello1 extends Livello {

    public Livello1() { super(1); }

    @Override
    protected void inizializza() {
        stanze.get(0).add(NemicoFactory.creaNemico(1, 50, 50));
        stanze.get(0).add(NemicoFactory.creaNemico(1, 150, 100));

        stanze.get(1).add(NemicoFactory.creaNemico(1, 80, 60));
        stanze.get(1).add(NemicoFactory.creaNemico(1, 160, 80));

        boss = new MostroTilapia(128, 88);
    }

}
