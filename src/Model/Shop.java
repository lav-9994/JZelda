package Model;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe che rappresenta lo shop del gioco.
 * Appare tra un livello e l'altro e vende carote a Link.
 */

public class Shop {
	 /** Lista degli item disponibili nello shop. */
    private List<Item> itemDisponibili;
    /** Prezzo di una carota in rupie. */
    public static final int PREZZO_CAROTA = 5;
    
    /**
     * Costruttore di Shop.
     * Inizializza lo shop con le carote disponibili.
     */
    public Shop() {
        itemDisponibili = new ArrayList<>();
        itemDisponibili.add(new Carote());
        itemDisponibili.add(new Carote());
        itemDisponibili.add(new Carote());
    }
    
    /**
     * Link acquista una carota se ha abbastanza rupie.
     *
     * @param link il personaggio principale
     * @return true se l'acquisto è andato a buon fine
     */
    public boolean acquista(Link link) {
        if (link.getRupie() >= PREZZO_CAROTA) {
            link.rimuoviRupie(PREZZO_CAROTA);
            link.aggiungiCarote(1);
            return true;
        }
        return false;
    }
    /**
     * Restituisce gli item disponibili nello shop usando Stream.
     *
     * @return lista degli item disponibili
     */
    public List<Item> getItemDisponibili() {
        return itemDisponibili.stream()
            .collect(Collectors.toList());
    }

}
