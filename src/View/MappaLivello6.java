package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 6, il bosco di Morgana.
 * Il terreno e' un prato, in gran parte fiorito; gli alberi, fitti al punto
 * da lasciare solo un sentiero, bloccano il passaggio.
 */
public class MappaLivello6 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.PRATO,  carica("prato.png"));
        immagini.put(Tile.FIORI,  carica("fiori.png"));
        immagini.put(Tile.ALBERO, carica("albero.png"));
    }
}
