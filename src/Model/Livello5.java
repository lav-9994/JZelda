package Model;

	/**
	 * Livello 5 del gioco. Boss: Gandalf.
	 */
	public class Livello5 extends Livello {

	    public Livello5() { super(5); }

	    @Override
	    protected void inizializza() {
	        stanze.get(0).add(NemicoFactory.creaNemico(5, 50, 50));
	        stanze.get(0).add(NemicoFactory.creaNemico(5, 150, 100));
	        stanze.get(0).add(NemicoFactory.creaNemico(5, 100, 130));
	        stanze.get(0).add(NemicoFactory.creaNemico(5, 200, 70));

	        stanze.get(1).add(NemicoFactory.creaNemico(5, 80, 60));
	        stanze.get(1).add(NemicoFactory.creaNemico(5, 160, 80));
	        stanze.get(1).add(NemicoFactory.creaNemico(5, 120, 120));
	        stanze.get(1).add(NemicoFactory.creaNemico(5, 60, 140));

	        boss = new Gandalf(128, 88);
	    }

}
