package Model;
/**
 * Tipi di tile che compongono le mappe dei livelli.
 * Ogni tile sa se può essere attraversato dai personaggi e quale
 * valore lo rappresenta nei file mappa.
 * Non contiene grafica: l'associazione tile-sprite sta nella View per mantenere la struttura MVC.
 */
public enum Tile {
	/** Sabbia: calpestabile. */
    SABBIA(0, true),
    /** Acqua: blocca il movimento. */
    ACQUA(1, false),
    /** Roccia: blocca il movimento. */
    ROCCIA(2, false);
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
    /** @return true se i personaggi possono camminare su questo tile */
    public boolean isCalpestabile() { return calpestabile; }

    /** @return il valore usato nei file mappa */
    public int getValoreMappa() { return valoreMappa; }
}



