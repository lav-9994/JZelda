package Model;

import Config.GameConstants;

/**
 * Classe astratta che rappresenta un boss che combatte in mischia.
 * Si muove verso Link mantenendo una posizione di base.
 * Alla morte spawna coniglietti.
 */

public abstract class BossMischia extends Nemico implements SpawnaConiglietti {

    /** Posizione x iniziale del boss. */
    protected int xBase;
    /** Posizione y iniziale del boss. */
    protected int yBase;
    /** Range massimo di allontanamento dalla posizione base in pixel. */
    protected static final int RANGE_BOSS = GameConstants.TILE_SIZE * 5;
    
    /** Tempo in millisecondi tra un attacco e l'altro. */
    protected long cooldownAttacco;
    
    /** Timestamp dell'ultimo attacco. */
    protected long ultimoAttacco;
    

    /**
     * Costruttore di BossMischia.
     *
     * @param x      posizione iniziale x
     * @param y      posizione iniziale y
     * @param vita   punti vita
     * @param danno  danno inflitto
     * @param width  larghezza in pixel
     * @param height altezza in pixel
     *  @param cooldownAttacco millisecondi tra un attacco e l'altro
     */
    public BossMischia(int x, int y, int vita, int danno, int width, int height,long cooldownAttacco) {
        super(x, y, vita, danno, width, height);
        this.xBase = x;
        this.yBase = y;
        this.cooldownAttacco = cooldownAttacco;
        this.ultimoAttacco = 0;
    }
    
    /**
     * Se può attaccare si muove verso Link, altrimenti torna alla posizione iniziale.
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    public void aggiorna(int linkX, int linkY) {
        if (puoAttaccare()) {
            muoviVersoLink(linkX, linkY);
        } else {
            if (x < xBase) x += GameConstants.VELOCITA_NEMICO;
            else if (x > xBase) x -= GameConstants.VELOCITA_NEMICO;
            if (y < yBase) y += GameConstants.VELOCITA_NEMICO;
            else if (y > yBase) y -= GameConstants.VELOCITA_NEMICO;
        }
    }

    /**
     * Muove il boss verso Link rispettando il range dalla posizione iniziale.
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    public void muoviVersoLink(int linkX, int linkY) {
        if (Math.abs(x - xBase) < RANGE_BOSS) {
            if (x < linkX) x += GameConstants.VELOCITA_NEMICO;
            else if (x > linkX) x -= GameConstants.VELOCITA_NEMICO;
        }
        if (Math.abs(y - yBase) < RANGE_BOSS) {
            if (y < linkY) y += GameConstants.VELOCITA_NEMICO;
            else if (y > linkY) y -= GameConstants.VELOCITA_NEMICO;
        }
    }
    
    /**
     * Movimento base — usa aggiorna dal Controller.
     */
    @Override
    public void muovi() {
    	
    }
    /**
     * Controlla se il boss può attaccare in base al cooldown.
     *
     * @return true se può attaccare
     */
    public boolean puoAttaccare() {
        return System.currentTimeMillis() - ultimoAttacco > cooldownAttacco;
    }
    
    /**
     * Il boss attacca Link se il cooldown è scaduto.
     *
     * @param link il personaggio principale
     */
    public void attacca(Link link) {
        if (!puoAttaccare()) return;
        link.subisciDanno(danno);
        ultimoAttacco = System.currentTimeMillis();
        setChanged();
        notifyObservers("attacco");
    }
   

    /**
     * Alla morte notifica lo spawn di coniglietti.
     *
     * @param danno quantità di danno subito
     */
    
    @Override
    public void subisciDanno(int danno) {
        super.subisciDanno(danno);
        if (!isVivo() && vivo) {
            vivo = false;
            setChanged();
            notifyObservers("morte_boss_mischia");
        }
    }
    /** 
     * Restituisce la posizione x iniziale del boss.
     * @return la posizione x iniziale del boss */
    public int getXBase() { return xBase; }
   
    /** 
     * Restituisce la posizione y iniziale del boss.
     * @return la posizione y iniziale del boss */
    public int getYBase() { return yBase; }
}




