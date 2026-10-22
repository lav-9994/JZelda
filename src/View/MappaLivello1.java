package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 1, ambientazione marina.
 */
public class MappaLivello1 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.SABBIA, carica("sabbia.png"));
        immagini.put(Tile.ACQUA,  carica("acqua.png"));
    }
}
