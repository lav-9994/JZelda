package Model;

/**
 * Enum deglo eventi che vengono mandati agli observer.
 * Inizialmente avevo messo delle stringhe ma mi sono resa conto che un errore di battitura poteva creare problemi, 
 * e ho optato per un enum di eventi.
 * Alcuni eventi non li gestisce nessuno in modo esplicito: fanno comunque
 * ridisegnare la schermata, perche' GameView li raccoglie tutti nel ramo finale del suo switch.
 */
public enum Evento {

    /** I punti vita di Link sono cambiati. */
    VITA,
    /** Il numero di vite di Link e' cambiato. */
    VITE,
    /** Link ha alzato o abbassato lo scudo che ha in mano. */
    SCUDO,
    /** Link ha cambiato scudo. Al momento nel codice è presente scudo base ma così lascio aperta la possibilià di averne altri potenziati in futuro */
    SCUDO_EQUIPAGGIATO,
    /** Link ha cambiato spada. Al momento ci sono una spada base e uan spada potenziata*/
    SPADA,
    /** Il danno che Link fa quando attacca aumenta. */
    BONUS_DANNO,
    /** Link ha raccolto un cristallo. */
    CRISTALLO,
    /** Le rupie di Link sono cambiate. */
    RUPIE,
    /** Le carote di Link sono cambiate. */
    CAROTE,
    /** Link ha esaurito le vite. */
    GAME_OVER,
    /** Link ha vibrato un colpo di spada. */
    ATTACCO_LINK,

    /** Un personaggio ha subito danno. */
    DANNO_SUBITO,
    /** Un nemico base e' morto: il numero di rupie lasciate lo tiene lui. */
    MORTE_NEMICO,
    /** Un boss di mischia e' morto. */
    MORTE_BOSS_MISCHIA,
    /** Un boss a distanza e' morto. */
    MORTE_BOSS_DISTANZA,
    /** Un boss di mischia ha colpito Link. */
    ATTACCO_BOSS,

    /** Il coniglietto speciale ha ridato la vita a Link. */
    VITA_BONUS,
    /** Il coniglietto speciale ha ferito Link. */
    DANNO_CONIGLIO,
    /** Un coniglietto ha lasciato la stanza o ha finito il suo compito. */
    CONIGLIO_DISATTIVATO,

    /** E' stato creato un profilo nuovo. */
    PROFILO,
    /** Le statistiche di un profilo sono cambiate. */
    STATISTICHE
}
