package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 2, un cortile di pietra.
 * Un'unica tile calpestabile: nessun ostacolo in questo livello.
 */
public class MappaLivello2 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.PIETRE, carica("pietre.png"));
    }
}
