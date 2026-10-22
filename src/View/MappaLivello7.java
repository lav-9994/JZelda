package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 7, il castello in rovina di LinkCattivo.
 * Il pavimento e' di marmo chiaro; i cumuli di macerie bloccano il passaggio
 * nelle prime due schermate, mentre la sala del trono dove attende il boss
 * resta sgombra.
 */
public class MappaLivello7 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.CASTELLO, carica("castello.png"));
        immagini.put(Tile.ROVINE,   carica("rovine.png"));
    }
}
