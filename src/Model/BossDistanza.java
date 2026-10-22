package Model;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe astratta che rappresenta un boss che attacca a distanza.
 * Resta fermo e lancia proiettili verso Link.
 * Alla morte spawna coniglietti.
 */
public abstract class BossDistanza extends Nemico implements SpawnaConiglietti {

    /** Lista dei proiettili attivi sparati dal boss. */
    protected List<Proiettile> proiettili;

    /** Tempo in millisecondi tra un attacco e l'altro. */
    protected long cooldownAttacco;

    /** Timestamp dell'ultimo attacco. */
    protected long ultimoAttacco;
    
    /** Intervallo tra un proiettile e l'altro durante la raffica in ms. */
    private final long intervalloRaffica;
    
    /** Durata della fase di attacco in ms. */
    private final long durataAttacco;
    
    /** Durata della pausa in ms. */
    private final long durataPausa;
    
    /** Indica se il boss sta attaccando o è in pausa. */
    private boolean faseAttacco;
    
    /** Timestamp inizio della fase corrente. */
    private long tempoInizioFase;
    
    /**
     * Costruttore di BossDistanza.
     *
     * @param x                posizione iniziale x
     * @param y                posizione iniziale y
     * @param vita             punti vita
     * @param danno            danno inflitto dai proiettili
     * @param larghezza            larghezza in pixel di mondo
     * @param altezza           altezza in pixel di mondo
     * @param intervalloRaffica millisecondi tra un proiettile e l'altro
     * @param durataAttacco    durata della fase di attacco in ms
     * @param durataPausa      durata della pausa in ms
     */
    public BossDistanza(int x, int y, int vita, int danno, int larghezza, int altezza,
            long intervalloRaffica, long durataAttacco, long durataPausa) {
    	 super(x, y, vita, danno, larghezza, altezza);
         this.proiettili = new ArrayList<>();
         this.ultimoAttacco = 0;
         this.intervalloRaffica = intervalloRaffica;
         this.durataAttacco = durataAttacco;
         this.durataPausa = durataPausa;
         this.faseAttacco = true;
         this.tempoInizioFase = System.currentTimeMillis();
     }
    /**
     * Movimento base, gestito dal Controller.
     */
    @Override
    public void muovi() {

    }

    /**
     * Fa ripartire il ciclo raffica/pausa dall'inizio.
     * Va chiamato quando Link entra nell'arena: il cronometro parte alla costruzione del livello, quindi senza questo il boss 
     * si troverebbe la fase di attacco gia' scaduta e resterebbe fermo per tutta la pausa prima del primo colpo.
     */
    public void iniziaCombattimento() {
        this.faseAttacco = true;
        this.tempoInizioFase = System.currentTimeMillis();
        this.ultimoAttacco = 0;
    }

    

    /**
     * Spara proiettili verso Link seguendo il ciclo raffica/pausa.
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    public void spara(int linkX, int linkY) {
        long now = System.currentTimeMillis();
        if (faseAttacco) {
            if (now - tempoInizioFase > durataAttacco) {
                faseAttacco = false;
                tempoInizioFase = now;
                return;
            }
            if (now - ultimoAttacco > intervalloRaffica) {
                Direzione dir;
                if (Math.abs(linkX - x) > Math.abs(linkY - y)) {
                    dir = linkX > x ? Direzione.DESTRA : Direzione.SINISTRA;
                } else {
                    dir = linkY > y ? Direzione.GIU : Direzione.SU;
                }
                proiettili.add(new Proiettile(x, y, dir, danno));
                ultimoAttacco = now;
            }
        } else {
            if (now - tempoInizioFase > durataPausa) {
                faseAttacco = true;
                tempoInizioFase = now;
            }
        }
    }
    /**
     * Rimuove i proiettili non più attivi usando Stream.
     */
    public void rimuoviProiettiliInattivi() {
        proiettili = proiettili.stream()
                .filter(Proiettile::isAttivo)
                .collect(Collectors.toList());
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
            notifyObservers(Evento.MORTE_BOSS_DISTANZA);
        }
    }

    /**
     * Indica se il boss e' nella fase di attacco.
     *
     * @return true se il boss e' nella fase di attacco
     */
    public boolean isFaseAttacco() { return faseAttacco; }
    /**
     * Restituisce la lista dei proiettili sparati.
     *
     * @return la lista dei proiettili sparati
     */
    public List<Proiettile> getProiettili() { return proiettili; }

    }
    



    