package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 3, un pavimento a mosaico romano.
 * Le colonne sono l'ostacolo che blocca il passaggio.
 */
public class MappaLivello3 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.MOSAICO, carica("mosaico.png"));
        immagini.put(Tile.COLONNA, carica("colonna.png"));
    }
}
