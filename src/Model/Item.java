package Model;
/**
 * Interfaccia per gli item del gioco. Ogni item ha un nome e un effetto applicabile su Link.
 */

public interface Item {

    /**
     * Restituisce il nome dell'item.
     *
     * @return nome dell'item
     */
    String getNome();
    /**
     * Applica l'effetto dell'item su Link.
     *
     * @param link il personaggio su cui applicare l'effetto
     */
    void applicaEffetto(Link link);


}
