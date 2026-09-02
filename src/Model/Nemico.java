package Model;
/**
 * Classe astratta che rappresenta un nemico del gioco.
 * Estende Personaggio e definisce il comportamento base dei nemici,
 * incluso il drop di rupie alla morte.
 */
public abstract class Nemico extends Personaggio {
	    /** Indica se il nemico è ancora vivo. */
    protected boolean vivo;
    /**
     * Costruttore di Nemico.
     *
     * @param x             posizione iniziale x
     * @param y             posizione iniziale y
     * @param vita          punti vita
     * @param danno         danno inflitto
     * @param width         larghezza in pixel
     * @param height        altezza in pixel
     */
    public Nemico(int x, int y, int vita, int danno, int width, int height) {
        super(x, y, vita, danno, width, height);
                this.vivo = true;
    }
    
    /**
     * Muove il nemico. Implementazione definita dalle sottoclassi.
     */
    @Override
    public abstract void muovi();
    /**
     * Infligge danno fisico a un personaggio.
     *
     * @param p il personaggio che subisce il danno
     */
    public void infliggiDanno(Personaggio p) {
        p.subisciDanno(this.danno);
    }
    /**
     * Il nemico subisce danno. La gestione della morte è delegata alle sottoclassi.
     *
     * @param danno quantità di danno subito
     */
    @Override
    public void subisciDanno(int danno) {
        super.subisciDanno(danno);
    }
    public boolean isVivoFlag() { return vivo; }


}
