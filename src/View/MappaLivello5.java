package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 5, il sotterraneo di Gandalf.
 * Il pavimento e' in pietra; gli scaffali pieni di pozioni e grimori,
 * disposti come le librerie del livello 4, bloccano il passaggio.
 */
public class MappaLivello5 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.SOTTERRANEO, carica("pavimento_mago.png"));
        immagini.put(Tile.SCAFFALE,    carica("libreria_mago.png"));
    }
}
