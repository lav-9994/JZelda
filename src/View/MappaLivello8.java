package View;

import Model.Tile;

/**
 * Grafica della mappa del livello 8, le terre vulcaniche di Sauron.
 * Il terreno e' roccia lavica percorsa da crepe incandescenti; le pozze di
 * lava, sempre piu' fitte man mano che ci si avvicina al boss, bloccano il
 * passaggio.
 */
public class MappaLivello8 extends MappaView {

    @Override
    protected void caricaImmagini() {
        immagini.put(Tile.ROCCIA, carica("terreno_vulcano.png"));
        immagini.put(Tile.LAVA,   carica("vulcano.png"));
    }
}
