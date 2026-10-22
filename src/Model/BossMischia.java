package Model;

import Config.GameConstants;

/**
 * Classe astratta che rappresenta un boss che combatte in mischia. Viene estesa dalle sottoclassi
 * che rappresentano i boss dei vari livelli. Implementa l'interfaccia SpawnaConiglietti che fa spuntare coniglietti alla morte.
 * Si muove verso Link mantenendo una posizione di base.
 */

public abstract class BossMischia extends Nemico implements SpawnaConiglietti {

    /** Posizione x iniziale del boss. */
    protected int xBase;
    /** Posizione y iniziale del boss. */
    protected int yBase;
    /** Range massimo di allontanamento dalla posizione base in pixel di mondo. */
    protected static final int RANGE_BOSS = GameConstants.TILE_SIZE * 8;
    
    /** Tempo in millisecondi tra un attacco e l'altro. */
    protected long cooldownAttacco;
    
    /** Timestamp dell'ultimo attacco. */
    protected long ultimoAttacco;

    /** Frame trascorsi nel fotogramma corrente della camminata. */
    private int frameTimer = 0;

    /** Fotogramma corrente del ciclo di camminata. */
    private int fotogramma = 0;

    /** Durata in frame di ogni fotogramma della camminata. */
    private static final int FRAME_PER_FOTOGRAMMA = 16;

    /** Numero di fotogrammi che compongono il ciclo di camminata. */
    private static final int FOTOGRAMMI_CAMMINATA = 2;

    /** Millisecondi per cui, dopo un colpo, resta visibile la posa di attacco. */
    private static final long DURATA_POSA_ATTACCO = 700;


    /**
     * Costruttore di BossMischia.
     *
     * @param x      posizione iniziale x
     * @param y      posizione iniziale y
     * @param vita   punti vita
     * @param danno  danno inflitto
     * @param larghezza  larghezza in pixel di mondo
     * @param altezza altezza in pixel di mondo
     *  @param cooldownAttacco millisecondi tra un attacco e l'altro
     */
    public BossMischia(int x, int y, int vita, int danno, int larghezza, int altezza,long cooldownAttacco) {
        super(x, y, vita, danno, larghezza, altezza);
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
        int xPrima = x;
        int yPrima = y;

        if (puoAttaccare()) {
            muoviVersoLink(linkX, linkY);
        } else {
            if (x < xBase) x += GameConstants.VELOCITA_NEMICO;
            else if (x > xBase) x -= GameConstants.VELOCITA_NEMICO;
            if (y < yBase) y += GameConstants.VELOCITA_NEMICO;
            else if (y > yBase) y -= GameConstants.VELOCITA_NEMICO;
        }

        avanzaCamminata(x != xPrima || y != yPrima);
    }

    /**
     * Fa avanzare il ciclo di camminata di un frame.
     * Da fermo torna al primo fotogramma, la posa di riposo, invece di
     * restare congelato a meta' passo.
     *
     * @param siStaMuovendo true se in questo frame il boss si e' spostato
     */
    private void avanzaCamminata(boolean siStaMuovendo) {
        if (!siStaMuovendo) {
            frameTimer = 0;
            fotogramma = 0;
            return;
        }
        frameTimer++;
        if (frameTimer >= FRAME_PER_FOTOGRAMMA) {
            frameTimer = 0;
            fotogramma = (fotogramma + 1) % FOTOGRAMMI_CAMMINATA;
        }
    }

    /**
     * Muove il boss verso Link rispettando il range dalla posizione iniziale.
     * Il range si controlla sulla destinazione del passo e non sulla posizione attuale, come gia' si fa per le tile che bloccano: 
     * un controllo sulla posizione attuale vieterebbe entrambi i versi una volta raggiunto il
     * bordo, e il boss resterebbe incastrato li' senza poter piu' tornare indietro. 
     * Cosi' invece si rifiuta solo il passo che uscirebbe dal range, mentre quello che riavvicina alla base e' sempre concesso.
     * Aggiorna anche la direzione in cui il boss guarda, scegliendo l'asse lungo cui Link e' piu' lontano: cosi' in diagonale non traballa
     * cambiando posa a ogni frame.
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    public void muoviVersoLink(int linkX, int linkY) {
        int passoX = 0;
        if (x < linkX) passoX = GameConstants.VELOCITA_NEMICO;
        else if (x > linkX) passoX = -GameConstants.VELOCITA_NEMICO;
        if (Math.abs(x + passoX - xBase) <= RANGE_BOSS) x += passoX;

        int passoY = 0;
        if (y < linkY) passoY = GameConstants.VELOCITA_NEMICO;
        else if (y > linkY) passoY = -GameConstants.VELOCITA_NEMICO;
        if (Math.abs(y + passoY - yBase) <= RANGE_BOSS) y += passoY;

        if (Math.abs(linkX - x) >= Math.abs(linkY - y)) {
            direzione = (linkX < x) ? Direzione.SINISTRA : Direzione.DESTRA;
        } else {
            direzione = (linkY < y) ? Direzione.SU : Direzione.GIU;
        }
    }
    
    /**
     * Movimento base, usa aggiorna dal Controller.
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
        notifyObservers(Evento.ATTACCO_BOSS);
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
            notifyObservers(Evento.MORTE_BOSS_MISCHIA);
        }
    }
    /**
     * Indica se il boss ha colpito da poco, rilevante per capire se va quindi disegnato nella posa di attacco invece che in quella di camminata.
     *
     * @return true se sono passati meno di DURATA_POSA_ATTACCO millisecondi
     *         dall'ultimo colpo andato a segno
     */
    public boolean staAttaccando() {
        return ultimoAttacco > 0
            && System.currentTimeMillis() - ultimoAttacco < DURATA_POSA_ATTACCO;
    }

    /**
     * Restituisce il fotogramma corrente del ciclo di camminata.
     * @return indice del fotogramma, da 0 a FOTOGRAMMI_CAMMINATA - 1 */
    public int getFotogramma() { return fotogramma; }

    /**
     * Restituisce la posizione x iniziale del boss.
     * @return la posizione x iniziale del boss */
    public int getXBase() { return xBase; }
   
    /** 
     * Restituisce la posizione y iniziale del boss.
     * @return la posizione y iniziale del boss */
    public int getYBase() { return yBase; }
}




