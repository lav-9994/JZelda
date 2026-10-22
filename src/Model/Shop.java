package Model;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe che rappresenta lo shop del gioco.
 * Appare alla conclusione di ogni livello. Al momento vende solo carote ma in futuro potrebbero esserci altri item.
 */

public class Shop {
	 /**
	  * Catalogo di cio' che lo shop vende: dice quali item sono in vendita,
	  * non quanti ce ne sono. Comprare non consuma il catalogo, quindi si possono acquistare infinite carote.
	  */
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
    }

    /**
     * Link acquista una carota se ha abbastanza rupie. L'effetto dell'item venduto non e' scritto qui: lo shop rimuove le rupie 
     * 
     *  dall'inventario di link e poi chiede
     * all'item di applicarsi l'effetto. Cosi' per mettere in vendita qualcos'altro
     * basta aggiungerlo al catalogo, senza toccare questo metodo.
     *
     * @param link il personaggio principale
     * @return true se l'acquisto è andato a buon fine
     */
    public boolean acquista(Link link) {
        if (itemDisponibili.isEmpty() || link.getRupie() < PREZZO_CAROTA) return false;
        link.rimuoviRupie(PREZZO_CAROTA);
        itemDisponibili.get(0).applicaEffetto(link);
        return true;
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
