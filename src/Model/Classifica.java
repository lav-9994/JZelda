package Model;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Classe che gestisce la classifica dei giocatori.
 * Ordina i profili per livelli completati in ordine decrescente.
 */
public class Classifica {
	 /** Riferimento al profilo per accedere ai dati dei giocatori. */
    private Profilo profilo;

    /**
     * Costruttore di Classifica.
     *
     * @param profilo il profilo da cui leggere i dati
     */
    public Classifica(Profilo profilo) {
        this.profilo = profilo;
    }
    /**
     * Restituisce la lista dei profili ordinati per vittorie in ordine decrescente.
     *
     * @return lista ordinata di ProfiloEntry
     */
    public List<Profilo.ProfiloEntry> getClassifica() {
        return profilo.getProfili().stream()
                .sorted((a, b) -> Integer.compare(b.getVinte(), a.getVinte()))
                .collect(Collectors.toList());
    }


}
