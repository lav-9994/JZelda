package Model;
/**
 * Tipi di tile che compongono le mappe dei livelli.
 * Ogni tila sa se può essere attraversata dai personaggi e quale valore la rappresenta nei file mappa.
 * Non contiene grafica: l'associazione tile-sprite sta nella View per mantenere la struttura MVC.
 */
public enum Tile {
	/** Sabbia: calpestabile. */
    SABBIA(0, true),
    /** Acqua: blocca il movimento. */
    ACQUA(1, false),
    /** Pietre: calpestabile. */
    PIETRE(2, true),
    /** Mosaico: calpestabile. */
    MOSAICO(3, true),
    /** Colonna: blocca il movimento. */
    COLONNA(4, false),
    /** Biblioteca: il pavimento del livello 4, calpestabile. */
    BIBLIOTECA(5, true),
    /** Libreria: blocca il movimento. */
    LIBRERIA(6, false),
    /** Sotterraneo: il pavimento di pietra del livello 5, calpestabile. */
    SOTTERRANEO(7, true),
    /** Scaffale: blocca il movimento. */
    SCAFFALE(8, false),
    /** Prato: l'erba del bosco del livello 6, calpestabile. */
    PRATO(9, true),
    /** Fiori: erba fiorita, calpestabile come il prato. */
    FIORI(10, true),
    /** Albero: blocca il movimento. */
    ALBERO(11, false),
    /** Roccia: il terreno vulcanico del livello 8, calpestabile. */
    ROCCIA(12, true),
    /** Lava: blocca il movimento. */
    LAVA(13, false),
    /** Castello: il pavimento di marmo del livello 7, calpestabile. */
    CASTELLO(14, true),
    /** Rovine: cumuli di macerie che bloccano il movimento. */
    ROVINE(15, false);
	/** Valore che rappresenta questo tile nei file mappa. */
    private final int valoreMappa;
    /** True se i personaggi possono attraversare il tile. */
    private final boolean calpestabile;
    /**
     * Costruttore delle costanti dell'enum.
     * Non può essere pubblico: le uniche istanze esistenti
     * sono quelle elencate sopra.
     *
     * @param valoreMappa  valore usato nei file mappa
     * @param calpestabile true se il tile è attraversabile
     */
    Tile(int valoreMappa, boolean calpestabile) {
        this.valoreMappa = valoreMappa;
        this.calpestabile = calpestabile;
    }
    /**
     * Restituisce il tile corrispondente a un valore letto dal file mappa.
     * Non crea nulla: cerca fra le costanti già esistenti.
     *
     * @param valore valore letto dal file mappa
     * @return il tile con quel valore
     * @throws IllegalArgumentException se nessun tile usa quel valore
     */
    public static Tile cercaPerValore(int valore) {
        for (Tile t : values()) {
            if (t.valoreMappa == valore) return t;
        }
        throw new IllegalArgumentException(
            "Valore di tile non valido nella mappa: " + valore);
    }
    /**
     * Indica se i personaggi possono camminare su questo tile.
     *
     * @return true se i personaggi possono camminare su questo tile
     */
    public boolean isCalpestabile() { return calpestabile; }

    /**
     * Restituisce il valore usato nei file mappa.
     *
     * @return il valore usato nei file mappa
     */
    public int getValoreMappa() { return valoreMappa; }
}



