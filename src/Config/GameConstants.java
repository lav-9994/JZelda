package Config;
/**
 * Classe contenente le costanti globali del gioco.
 */
public class GameConstants {
    /** Dimensione di una singola tile in pixel. */
    public static final int TILE_SIZE = 16;

    /** Numero di tile orizzontali visibili a schermo. */
    public static final int SCHERMATA_TILE_W = 16;
    /** Numero di tile verticali visibili a schermo. */
    public static final int SCHERMATA_TILE_H = 11;
    /** Larghezza di una schermata in pixel. */
    public static final int SCHERMATA_W = TILE_SIZE * SCHERMATA_TILE_W;
    /** Altezza di una schermata in pixel. */
    public static final int SCHERMATA_H = TILE_SIZE * SCHERMATA_TILE_H;
    /** Numero di schermate che compongono un livello. */
    public static final int NUM_SCHERMATE = 3;

    /** Numero di livelli del gioco. */
    public static final int NUM_LIVELLI = 8;

    /** Velocità di movimento di Link in pixel. */
    public static final int VELOCITA_LINK = 2;
    /** Velocità di movimento dei nemici base in pixel. */
    public static final int VELOCITA_NEMICO = 2;
    /** Range di movimento dei nemici base in pixel (3 tile). */
    public static final int RANGE_NEMICO = TILE_SIZE * 3;
    /** Velocità di movimento dei coniglietti in pixel. */
    public static final int VELOCITA_CONIGLIO = 2;
    /** Danno inflitto dai coniglietti speciali. */
    public static final int DANNO_CONIGLIO = 2;
    /** Velocità dei proiettili in pixel. */
    public static final int VELOCITA_PROIETTILE = 4;

    /** Larghezza della mappa in tile (3 schermate affiancate). */
    public static final int MAPPA_TILE_W = SCHERMATA_TILE_W * NUM_SCHERMATE;
    /** Altezza della mappa in tile (uguale allo schermo). */
    public static final int MAPPA_TILE_H = SCHERMATA_TILE_H;
    /** Larghezza totale della mappa in pixel. */
    public static final int MAPPA_W = TILE_SIZE * MAPPA_TILE_W;
    /** Altezza totale della mappa in pixel. */
    public static final int MAPPA_H = TILE_SIZE * MAPPA_TILE_H;

	
}