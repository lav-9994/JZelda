package View;

import Model.Tile;
import Config.GameConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import javax.imageio.ImageIO;

/**
 * Disegna la mappa di un livello, tile per tile, tenendo conto della camera.
 * Ogni livello ha il proprio tema grafico: le sottoclassi si limitano a
 * dire quale sprite corrisponde a quale tile con caricaImmagini().
 * Se una sprite manca, la tile viene disegnata con un rettangolo colorato
 * invece che lasciare un buco, cosi' un errore di caricamento si vede
 * subito e non si confonde con uno sfondo nero.
 */
public abstract class MappaView {

    /** Immagine associata a ogni tipo di tile usato dal livello. */
    protected final EnumMap<Tile, BufferedImage> immagini = new EnumMap<>(Tile.class);

    /**
     * Costruttore di MappaView.
     * Chiama subito caricaImmagini(): ogni sottoclasse popola la propria
     * mappa tile-sprite prima che la View provi a disegnare qualsiasi cosa.
     */
    protected MappaView() {
        caricaImmagini();
    }

    /**
     * Associa ogni tile usato dal livello alla propria sprite.
     * Implementato dalle sottoclassi, una per livello.
     */
    protected abstract void caricaImmagini();

    /**
     * Carica una sprite dal classpath.
     * Non lancia mai eccezioni: se il file manca o e' illeggibile, ritorna
     * null e la tile ricade sul rettangolo colorato in disegna().
     *
     * @param nome nome del file dentro /sprites/
     * @return l'immagine caricata, o null se non trovata
     */
    protected BufferedImage carica(String nome) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/sprites/" + nome));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Sprite di mappa non trovata: " + nome);
            return null;
        }
    }

    /**
     * Disegna le tile della schermata attualmente inquadrata dalla camera.
     *
     * @param g       contesto grafico
     * @param mappa   la matrice di tile del livello, indicizzata [riga][colonna]
     * @param cameraX posizione della camera in pixel di mondo
     */
    public void disegna(Graphics g, Tile[][] mappa, int cameraX) {
        int colonnaIniziale = cameraX / GameConstants.TILE_SIZE;

        for (int riga = 0; riga < GameConstants.MAPPA_TILE_H; riga++) {
            for (int i = 0; i < GameConstants.SCHERMATA_TILE_W; i++) {
                int colonna = colonnaIniziale + i;
                if (colonna >= GameConstants.MAPPA_TILE_W) break;
                Tile tile = mappa[riga][colonna];
                if (tile == null) continue;

                int x = i * GameConstants.TILE_SU_SCHERMO;
                int y = riga * GameConstants.TILE_SU_SCHERMO + GameConstants.ALTEZZA_BARRA;
                BufferedImage sprite = immagini.get(tile);

                if (sprite != null) {
                    g.drawImage(sprite, x, y,
                        GameConstants.TILE_SU_SCHERMO, GameConstants.TILE_SU_SCHERMO, null);
                } else {
                    g.setColor(COLORE_DI_RISERVA);
                    g.fillRect(x, y, GameConstants.TILE_SU_SCHERMO, GameConstants.TILE_SU_SCHERMO);
                }
            }
        }
    }

    /** Colore di ripiego per qualunque tile senza sprite: lo stesso beige della sabbia. */
    private static final Color COLORE_DI_RISERVA = new Color(212, 195, 140);
}
