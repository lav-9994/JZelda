package Config;
/**
 * Classe contenente le costanti globali del gioco.
 */
public class GameConstants {
    /** Dimensione di una singola tile in pixel di mondo. */
    public static final int TILE_SIZE = 16;

    /** Numero di tile orizzontali visibili a schermo. */
    public static final int SCHERMATA_TILE_W = 16;
    /** Numero di tile verticali visibili a schermo. */
    public static final int SCHERMATA_TILE_H = 11;
    /** Larghezza di una schermata in pixel di mondo. */
    public static final int SCHERMATA_W = TILE_SIZE * SCHERMATA_TILE_W;
    /** Altezza di una schermata in pixel di mondo. */
    public static final int SCHERMATA_H = TILE_SIZE * SCHERMATA_TILE_H;
    /** Numero di schermate che compongono un livello. */
    public static final int NUM_SCHERMATE = 3;

    /** Numero di livelli del gioco. */
    public static final int NUM_LIVELLI = 8;

    /**
     * Quanti livelli lasciano un cristallo alla morte del boss, cioe' i primi
     * cinque. E' anche il numero di cristalli che servono per ottenere la
     * spada finale: sono per forza lo stesso numero, quindi e' scritto una
     * volta sola invece di due volte per conto proprio.
     */
    public static final int LIVELLI_CON_CRISTALLO = 5;

    /** Velocità di movimento di Link in pixel di mondo. */
    public static final int VELOCITA_LINK = 2;
    /** Velocità di movimento dei nemici base in pixel di mondo. */
    public static final int VELOCITA_NEMICO = 1;
    /** Frame di attesa tra uno spostamento e l'altro dei nemici base. */
    public static final int FRAME_PER_PASSO_NEMICO = 4;
    /** Range di movimento dei nemici base in pixel di mondo (1 tile). */
    public static final int RANGE_NEMICO = TILE_SIZE;
    /** Velocità di movimento dei coniglietti in pixel di mondo. */
    public static final int VELOCITA_CONIGLIO = 1;
    /** Danno inflitto dai coniglietti speciali. */
    public static final int DANNO_CONIGLIO = 2;
    /** Velocità dei proiettili in pixel di mondo. */
    public static final int VELOCITA_PROIETTILE = 4;

    /** Larghezza della mappa in tile (3 schermate affiancate). */
    public static final int MAPPA_TILE_W = SCHERMATA_TILE_W * NUM_SCHERMATE;
    /** Altezza della mappa in tile (uguale allo schermo). */
    public static final int MAPPA_TILE_H = SCHERMATA_TILE_H;
    /** Larghezza totale della mappa in pixel di mondo. */
    public static final int MAPPA_W = TILE_SIZE * MAPPA_TILE_W;
    /** Altezza totale della mappa in pixel di mondo. */
    public static final int MAPPA_H = TILE_SIZE * MAPPA_TILE_H;

    /** Quanti pixel di schermo vale un pixel di gioco. */
    public static final int FATTORE_CONVERSIONE = 3;
    /** Altezza in pixel di schermo della barra con vite, HP, rupie e carote. */
    public static final int ALTEZZA_BARRA = 32;
    /** Dimensione di una tile cosi' come viene mostrata a schermo. */
    public static final int TILE_SU_SCHERMO = TILE_SIZE * FATTORE_CONVERSIONE;

    /**
     * Costruttore privato: la classe contiene solo costanti e non va istanziata.
     */
    private GameConstants() { }

}