package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 4, la biblioteca di Barbero.
 * Il pavimento e' un tappeto rosso; le librerie, disposte a scaffalature
 * nelle prime due schermate, bloccano il passaggio.
 */
public class MappaLivello4 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.BIBLIOTECA, carica("tile_barbero.png"));
        immagini.put(Tile.LIBRERIA,   carica("tile_libreria.png"));
    }
}
