package Model;

/**
 * Scudo dato a Link all'inizio del gioco.
 * Dimezza i colpi diretti e ferma del tutto i proiettili.
 */
public class ScudoBase extends Scudo {

    /**
     * Costruttore di ScudoBase.
     */
    public ScudoBase() {
        super("Scudo");
    }

    /**
     * Dimezza il colpo, arrotondando per difetto.
     *
     * @param danno il danno di partenza
     * @return meta' del danno
     */
    @Override
    public int danniDaColpo(int danno) {
        return danno / 2;
    }

    /**
     * Ferma il proiettile: non passa niente.
     *
     * @param danno il danno di partenza, ignorato
     * @return zero
     */
    @Override
    public int danniDaProiettile(int danno) {
        return 0;
    }
}
