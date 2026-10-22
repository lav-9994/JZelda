package View;

/**
 * Mappa di riserva se un livello che non ha una grafica propria.
 * Non carica nessuna sprite, quindi disegna() ricade sui rettangoli colorati.
 *
 * Tutti gli otto livelli hanno ormai la loro MappaLivelloN, perciò in una
 * partita normale questa classe non viene mai istanziata. Esiste per chiudere
 * il ramo default di sceglieMappa restituendo comunque un oggetto valido:
 * è l'oggetto nullo del gruppo. Grazie a lei disegnaMappa può chiamare
 * mappaView.disegna(...) senza controllare ogni volta se la mappa esiste, e
 * un eventuale nono livello aggiunto senza grafica verrebbe disegnato a
 * rettangoli invece di far fallire il gioco.
 */
public class MappaProvvisoria extends MappaView {

    @Override
    protected void caricaImmagini() {
    }
}
