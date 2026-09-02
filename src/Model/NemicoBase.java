package Model;

import java.util.Random;
import Config.GameConstants;

/**
 * Classe che rappresenta i nemici minori presenti nelle prime due stanze
 * di ogni dungeon. Si muove a croce attorno alla posizione iniziale.
 * Alla morte droppa da 1 a 3 rupie casualmente.
 */
public class NemicoBase extends Nemico {

    /** Posizione x iniziale. */
    private int xIniziale;
    /** Posizione y iniziale. */
    private int yIniziale;
    /** Range massimo di allontanamento dalla posizione iniziale. */
    private static final int RANGE_MOVIMENTO = GameConstants.RANGE_NEMICO;

    /** Ciclo di direzioni: SU, GIU, SINISTRA, DESTRA. */
    private final Direzione[] ciclo = {Direzione.SU, Direzione.GIU, Direzione.SINISTRA, Direzione.DESTRA};
    /** Indice della fase corrente nel ciclo. */
    private int faseCorrente = 0;
    /** True se il nemico sta tornando al centro. */
    private boolean ritorno = false;

    /** Generatore di numeri casuali per il drop delle rupie. */
    private Random random;

    /**
     * Costruttore di NemicoBase.
     *
     * @param x     posizione iniziale x
     * @param y     posizione iniziale y
     * @param vita  punti vita
     * @param danno danno inflitto
     */
    public NemicoBase(int x, int y, int vita, int danno) {
        super(x, y, vita, danno, 16, 16);
        this.xIniziale = x;
        this.yIniziale = y;
        this.random = new Random();
    }

    /**
     * Muove il nemico a croce attorno alla posizione iniziale.
     */
    @Override
    public void muovi() {
        Direzione dir = ciclo[faseCorrente];

        if (!ritorno) {
            switch (dir) {
                case SU       -> y -= GameConstants.VELOCITA_NEMICO;
                case GIU      -> y += GameConstants.VELOCITA_NEMICO;
                case SINISTRA -> x -= GameConstants.VELOCITA_NEMICO;
                case DESTRA   -> x += GameConstants.VELOCITA_NEMICO;
            }
            if (raggiuntaLimite(dir)) ritorno = true;
        } else {
            switch (dir) {
                case SU       -> y += GameConstants.VELOCITA_NEMICO;
                case GIU      -> y -= GameConstants.VELOCITA_NEMICO;
                case SINISTRA -> x += GameConstants.VELOCITA_NEMICO;
                case DESTRA   -> x -= GameConstants.VELOCITA_NEMICO;
            }
            if (tornatoAlCentro(dir)) {
                x = xIniziale;
                y = yIniziale;
                ritorno = false;
                faseCorrente = (faseCorrente + 1) % ciclo.length;
            }
        }
    }

    private boolean raggiuntaLimite(Direzione dir) {
        return switch (dir) {
            case SU       -> yIniziale - y >= RANGE_MOVIMENTO;
            case GIU      -> y - yIniziale >= RANGE_MOVIMENTO;
            case SINISTRA -> xIniziale - x >= RANGE_MOVIMENTO;
            case DESTRA   -> x - xIniziale >= RANGE_MOVIMENTO;
        };
    }

    private boolean tornatoAlCentro(Direzione dir) {
        return switch (dir) {
            case SU       -> y >= yIniziale;
            case GIU      -> y <= yIniziale;
            case SINISTRA -> x >= xIniziale;
            case DESTRA   -> x <= xIniziale;
        };
    }

    /**
     * Restituisce la direzione effettiva di movimento (considerando il ritorno).
     *
     * @return direzione attuale di spostamento
     */
    public Direzione getDirezioneEffettiva() {
        if (!ritorno) return ciclo[faseCorrente];
        return switch (ciclo[faseCorrente]) {
            case SU       -> Direzione.GIU;
            case GIU      -> Direzione.SU;
            case SINISTRA -> Direzione.DESTRA;
            case DESTRA   -> Direzione.SINISTRA;
        };
    }

    /**
     * Il nemico subisce danno. Alla morte droppa da 1 a 3 rupie.
     *
     * @param danno quantità di danno subito
     */
    @Override
    public void subisciDanno(int danno) {
        super.subisciDanno(danno);
        if (!isVivo() && vivo) {
            vivo = false;
            int rupie = random.nextInt(3) + 1;
            setChanged();
            notifyObservers("morte:" + rupie);
        }
    }
}