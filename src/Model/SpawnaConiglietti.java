package Model;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
/**
 * Interfaccia implementata dai personaggi che spawnano coniglietti alla morte.
 */
public interface SpawnaConiglietti {

	/** Raggio in pixel di mondo entro cui i coniglietti nascono attorno al boss. */
	int SPARPAGLIAMENTO = 8;

	/**
     * Spawna un numero casuale di coniglietti (tra 4 e 7) nella posizione del personaggio.
     *
     * @param x posizione x dove spawnare
     * @param y posizione y dove spawnare
     * @return lista di coniglietti spawnati
     */
	 default List<Coniglietti> spawnaConiglietti(int x, int y) {
	        List<Coniglietti> lista = new ArrayList<>();
	        Random random = new Random();
	        int numero = random.nextInt(4) + 4;
	        for (int i = 0; i < numero; i++) {
	            int dx = random.nextInt(SPARPAGLIAMENTO * 2 + 1) - SPARPAGLIAMENTO;
	            int dy = random.nextInt(SPARPAGLIAMENTO * 2 + 1) - SPARPAGLIAMENTO;
	            lista.add(new Coniglietti(x + dx, y + dy));
	        }
	        int indiceSpeciale = random.nextInt(lista.size());
	        lista.get(indiceSpeciale).setSpeciale(true);
	        return lista;
	        
	 }
}

