package Model;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
/**
 * Interfaccia implementata dai personaggi che spawnano coniglietti alla morte.
 */
public interface SpawnaConiglietti {
	/**
     * Spawna un numero casuale di coniglietti (tra 4 e 7) nella posizione del personaggio.
     *
     * @param x posizione x dove spawnare
     * @param y posizione y dove spawnare
     * @return lista di coniglietti spawnati
     */
	 default List<Coniglietti> spawnaConiglietti(int x, int y) {
	        List<Coniglietti> lista = new ArrayList<>();
	        int numero = new Random().nextInt(4) + 4; // da 4 a 7
	        for (int i = 0; i < numero; i++) {
	            lista.add(new Coniglietti(x, y));
	        }
	     // uno solo è sempre speciale
	        int indiceSpeciale = new Random().nextInt(lista.size());
	        lista.get(indiceSpeciale).setSpeciale(true);
	        return lista;
	        
	 }
}

