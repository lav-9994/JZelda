package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Config.GameConstants;

/**
 * Classe astratta che rappresenta un generico livello del gioco.
 * Ogni livello ha una mappa di tile, alcune calpestabili altre no, un boss e una lista di nemici base (minion).
 */
public abstract class Livello {

    /** Numero del livello (1-8). */
    protected int numeroLivello;

    /** Mappa del livello: matrice di tile [righe][colonne]. */
    protected Tile[][] mappa =
        new Tile[GameConstants.MAPPA_TILE_H][GameConstants.MAPPA_TILE_W];

    /** Lista di tutti i nemici del livello. */
    protected List<NemicoBase> minion;

    /** Boss del livello. */
    protected Nemico boss;

    /** Rupie lasciate a terra dai nemici uccisi. */
    protected List<Rupia> rupie;

    /** Cristalli lasciati a terra dai boss sconfitti. */
    protected List<Cristallo> cristalli;

    /**
     * Costruttore di Livello.
     * Carica la mappa dal file corrispondente al numero del livello,
     * poi lascia alla sottoclasse il piazzamento di nemici e boss.
     *
     * @param numeroLivello numero del livello corrente
     */
    public Livello(int numeroLivello) {
        this.numeroLivello = numeroLivello;
        this.minion = new ArrayList<>();
        this.rupie = new ArrayList<>();
        this.cristalli = new ArrayList<>();
        caricaMappa();
        inizializza();
    }

    /**
     * Piazza i minion e il boss del livello.
     * Implementato da ogni sottoclasse.
     */
    protected abstract void inizializza();

    /**
     * Carica la mappa dal file di testo corrispondente al numero del livello.
     * Il file deve contenere MAPPA_TILE_H righe di MAPPA_TILE_W valori separati da spazi, dove ogni valore corrisponde a un Tile.
     * Se il file non esiste la mappa viene riempita di sabbia. Questa decisione è stata presa sia per mantenere giocabile il gioco 
     * anche in caso di errori di caricamento sia per avere un segnaposto quando si aggiunge un altro livello che fa partire il gioco
     * anche se non si sono ancora scelte le tile del livello.
     */
    private void caricaMappa() {
        String percorso = "/maps/livello" + numeroLivello + ".txt";
        InputStream streamMappa = getClass().getResourceAsStream(percorso);

        if (streamMappa == null) {
            System.err.println("Mappa non trovata: " + percorso
                + " - riempita di sabbia.");
            riempiDiSabbia();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(streamMappa))) {
            for (int riga = 0; riga < GameConstants.MAPPA_TILE_H; riga++) {
                String linea = br.readLine();
                if (linea == null) {
                    throw new IllegalStateException("La mappa " + percorso
                        + " ha meno di " + GameConstants.MAPPA_TILE_H + " righe");
                }
                String[] valori = linea.trim().split("\\s+");
                for (int col = 0; col < GameConstants.MAPPA_TILE_W; col++) {
                    mappa[riga][col] = Tile.cercaPerValore(Integer.parseInt(valori[col]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore leggendo la mappa " + percorso, e);
        }
    }

    /** Riempie tutta la mappa di sabbi se  il file manca. */
    private void riempiDiSabbia() {
        for (int riga = 0; riga < GameConstants.MAPPA_TILE_H; riga++) {
            for (int col = 0; col < GameConstants.MAPPA_TILE_W; col++) {
                mappa[riga][col] = Tile.SABBIA;
            }
        }
    }

    /**
     * Controlla se il livello è completato .
     *
     * @return true se il livello è completato
     */
    public boolean isCompletato() {
        return boss != null && !boss.isVivo();
    }

    /**
     * Lascia cadere una rupia a terra nel mondo.
     *
     * @param x      posizione x
     * @param y      posizione y
     * @param valore valore della rupia
     */
    public void creaRupia(int x, int y, int valore) {
        rupie.add(new Rupia(x, y, valore));
    }

    /**
     * Lascia cadere un cristallo a terra nel mondo.
     *
     * @param x posizione x
     * @param y posizione y
     */
    public void creaCristallo(int x, int y) {
        cristalli.add(new Cristallo(x, y));
    }

    /**
     * Toglie dal mondo gli oggetti che Link ha già raccolto.
     */
    public void rimuoviOggettiRaccolti() {
        rupie.removeIf(r -> r.isRaccolta());
        cristalli.removeIf(c -> c.isRaccolto());
    }

    /**
     * Restituisce il numero del livello.
     *
     * @return il numero del livello
     */
    public int getNumeroLivello()          { return numeroLivello; }
    /**
     * Restituisce il boss del livello.
     *
     * @return il boss del livello
     */
    public Nemico getBoss()                { return boss; }
    /**
     * Restituisce la matrice di tile del livello.
     *
     * @return la matrice di tile del livello
     */
    public Tile[][] getMappa()             { return mappa; }
    /**
     * Restituisce la lista dei nemici base.
     *
     * @return la lista dei nemici base
     */
    public List<NemicoBase> getNemici()    { return minion; }
    /**
     * Restituisce la lista delle rupie a terra.
     *
     * @return la lista delle rupie a terra
     */
    public List<Rupia> getRupie()          { return rupie; }
    /**
     * Restituisce la lista dei cristalli a terra.
     *
     * @return la lista dei cristalli a terra
     */
    public List<Cristallo> getCristalli()  { return cristalli; }
}
