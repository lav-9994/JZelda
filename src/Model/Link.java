package Model;

import Config.GameConstants;

/**
 * Classe che rappresenta il personaggio principale Link.
 * Gestisce movimento, attacco, inventario, vita e rupie.
 */

public class Link extends Personaggio { 
	 
	/** Numero di vite, aumenta a ogni livello completato. */
	public static int VITE = 3;
	/** Punti vita per ogni vita. */
	public static final int HP_PER_VITA = 4;
	/** Vite rimanenti. */
	private int vite;
	 private Spada spada;
    /**
     * Scudo equipaggiato. E' un'altra cosa da scudoAttivo: questo dice quale
     * scudo Link possiede, quello se in questo momento lo sta alzando.
     */
    private Scudo scudo;
    /** Bonus danno accumulato salendo di livello. */
    private int bonusDanno;
    /** Numero di rupie possedute. */
    private int rupie;
    /** Numero di carote nell'inventario. */
    private int carote;
    /** Numero di cristalli raccolti. */
    private int cristalli;
    /** Timestamp dell'ultimo danno ricevuto. */
    private long ultimoDanno = 0;
    /** Millisecondi di invulnerabilità dopo un colpo. */
    private static final long COOLDOWN_DANNO = 1500;
    /** Indica se lo scudo è attualmente attivo. */
    private boolean scudoAttivo;

    /** Indica se Link sta attaccando. */
    private boolean attaccando;

    /** Timestamp dell'ultimo cristallo raccolto. */
    private long ultimoCristallo = 0;

    /**
     * Millisecondi per cui, dopo aver raccolto un cristallo, Link resta fermo
     * a mostrarlo sopra la testa.
     */
    private static final long DURATA_POSA_CRISTALLO = 1500;

    /**
     * Costruttore di Link.
     * Parte con la spada base, senza scudo attivo e con l'inventario vuoto.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    public Link(int x, int y) {
        super(x, y, HP_PER_VITA, 1, 16, 16);
        this.vite = VITE;
        this.spada = new SpadaBase();
        this.scudo = new ScudoBase();
        this.bonusDanno = 0;
        this.rupie = 0;
        this.carote = 0;
        this.scudoAttivo = false;
        this.attaccando = false;
    }
    
    /**
     * Muove Link nella direzione corrente di una quantità fissa di pixel di mondo.
     * Non notifica niente, come non lo fanno i nemici quando si spostano: il
     * ciclo di gioco ridisegna a ogni frame comunque, quindi non c'e' nessuno
     * che debba reagire a un movimento.
     */
    @Override
    public void muovi() {
        switch (direzione) {
            case SU:    y -= GameConstants.VELOCITA_LINK; break;
            case GIU:   y += GameConstants.VELOCITA_LINK; break;
            case SINISTRA: x -= GameConstants.VELOCITA_LINK; break;
            case DESTRA:   x += GameConstants.VELOCITA_LINK; break;
        }
    }

    /**
     * Link attacca nella direzione corrente.
     * Con lo scudo alzato il colpo non parte: spada e scudo si escludono a
     * vicenda e vince chi era gia' attivo, quindi per attaccare bisogna prima
     * abbassare lo scudo. Il controllo sta qui e non nel Controller perche'
     * cosi' l'invariante vale per chiunque chiami il metodo.
     * Notifica gli observer dell'attacco.
     */
    public void attacca() {
        if (scudoAttivo) return;
        attaccando = true;
        setChanged();
        notifyObservers(Evento.ATTACCO_LINK);
    }

    /**
     * Termina l'animazione di attacco.
     */
    public void terminaAttacco() {
        attaccando = false;
    }

    /**
     * Attiva lo scudo di Link.
     * Mentre sta colpendo lo scudo non si alza: e' l'altra meta' della regola
     * per cui spada e scudo non possono essere usati insieme.
     * Notifica gli observer.
     */
    public void attivaScudo() {
        if (attaccando) return;
        scudoAttivo = true;
        setChanged();
        notifyObservers(Evento.SCUDO);
    }

    /**
     * Disattiva lo scudo.
     */
    public void disattivaScudo() {
        scudoAttivo = false;
        setChanged(); 
        notifyObservers(Evento.SCUDO);
    }

    /**
     * Link subisce danno. Con lo scudo alzato quanto ne passa lo decide lo
     * scudo che ha in mano, non questo metodo.
     * Link è invulnerabile per 1.5 secondi dopo aver ricevuto un colpo.
     * @param danno quantità di danno subito
     */
    @Override
    public void subisciDanno(int danno) {
        if (System.currentTimeMillis() - ultimoDanno < COOLDOWN_DANNO) return;
        ultimoDanno = System.currentTimeMillis();
        if (scudoAttivo) danno = scudo.danniDaColpo(danno);
        super.subisciDanno(danno);
        if (!isVivo()) {
            vite--;
            if (vite <= 0) {
                setChanged();
                notifyObservers(Evento.GAME_OVER);
            } else {
                vita = HP_PER_VITA;
                setChanged();
                notifyObservers(Evento.VITA);
            }
        } else {
            setChanged();
            notifyObservers(Evento.VITA);
        }
    }
    /**
     * Link subisce danno da proiettile. Con lo scudo alzato quanto ne passa
     * lo decide lo scudo: quello base li ferma del tutto.
     *
     * @param danno quantità di danno subito
     */
    public void subisciDannoProiettile(int danno) {
        if (System.currentTimeMillis() - ultimoDanno < COOLDOWN_DANNO) return;
        ultimoDanno = System.currentTimeMillis();
        if (scudoAttivo) danno = scudo.danniDaProiettile(danno);
        super.subisciDanno(danno);
        if (!isVivo()) {
            vite--;
            if (vite <= 0) {
                setChanged();
                notifyObservers(Evento.GAME_OVER);
            } else {
                vita = HP_PER_VITA;
                setChanged();
                notifyObservers(Evento.VITA);
            }
        } else {
            setChanged();
            notifyObservers(Evento.VITA);
        }
    }
    /**
     * Equipaggia una nuova spada.
     * Notifica gli observer.
     *
     * @param spada la spada da equipaggiare
     */
    public void equipaggiaSpada(Spada spada) {
        this.spada = spada;
        setChanged();
        notifyObservers(Evento.SPADA);
    }
    /**
     * Equipaggia un nuovo scudo.
     * Notifica gli observer.
     *
     * @param scudo lo scudo da equipaggiare
     */
    public void equipaggiaScudo(Scudo scudo) {
        this.scudo = scudo;
        setChanged();
        notifyObservers(Evento.SCUDO_EQUIPAGGIATO);
    }
    /**
     * Restituisce il danno totale inflitto da Link,
     * sommando il danno della spada e il bonus accumulato per livello.
     *
     * @return danno totale
     */
    public int getDannoTotale() {
        return spada.getDanno() + bonusDanno;
    }
    /**
     * Aumenta il bonus danno di Link al completamento di un livello.
     */
    public void aumentaDanno() {
        bonusDanno++;
        setChanged();
        notifyObservers(Evento.BONUS_DANNO);
    }
    /**
     * Aggiunge un cristallo all'inventario e notifica gli observer.
     * Segna anche il momento della raccolta: per un secondo e mezzo Link
     * resta fermo a mostrarlo. Spada e scudo vengono abbassati, perche' per
     * tenere il cristallo alzato servono tutte e due le mani.
     */
    public void aggiungiCristallo() {
        cristalli++;
        ultimoCristallo = System.currentTimeMillis();
        attaccando = false;
        scudoAttivo = false;
        setChanged();
        notifyObservers(Evento.CRISTALLO);

        // i cristalli esistenti sono uno per ognuno dei primi cinque livelli:
        // chi li trova tutti si guadagna la spada che fa il doppio del danno
        if (cristalli >= GameConstants.LIVELLI_CON_CRISTALLO) {
            new SpadaFinale().applicaEffetto(this);
        }
    }

    /**
     * Indica se Link sta ancora mostrando il cristallo appena raccolto.
     * Finche' e' vero il Controller ignora i comandi e la View disegna la
     * posa con il cristallo alzato.
     *
     * @return true se sono passati meno di DURATA_POSA_CRISTALLO millisecondi
     *         dalla raccolta dell'ultimo cristallo
     */
    public boolean staMostrandoCristallo() {
        return ultimoCristallo > 0
            && System.currentTimeMillis() - ultimoCristallo < DURATA_POSA_CRISTALLO;
    }


    /**
     * Link consuma una carota per recuperare una vita.
     * La carota viene consumata solo se l'inventario non è vuoto
     * e le vite non sono già al massimo.
     */
    public void usaCarota() {
        if (carote > 0 && vite < VITE) {
            carote--;
            vite++;
            vita = HP_PER_VITA;
            setChanged();
            notifyObservers(Evento.VITA);
        }
    }
    

    /**
     * Aggiunge rupie all'inventario di Link.
     *
     * @param quantita numero di rupie da aggiungere
     */
    public void aggiungiRupie(int quantita) {
        this.rupie += quantita;
        setChanged();
        notifyObservers(Evento.RUPIE);
    }

    /**
     * Rimuove rupie dall'inventario di Link.
     *
     * @param quantita numero di rupie da rimuovere
     */
    public void rimuoviRupie(int quantita) {
        this.rupie = Math.max(0, this.rupie - quantita);
        setChanged();
        notifyObservers(Evento.RUPIE);
    }

    /**
     * Aggiunge carote all'inventario.
     *
     * @param quantita numero di carote da aggiungere
     */
    public void aggiungiCarote(int quantita) {
        this.carote += quantita;
        setChanged();
        notifyObservers(Evento.CAROTE);
    }

    /**
     * Rimuove una carota dall'inventario.
     * Usato quando un coniglio mangia la carota.
     */
    public void rimuoviCarota() {
        if (carote > 0) carote--;
        setChanged();
        notifyObservers(Evento.CAROTE);
    }

   
    /**
     * Aggiunge una vita a Link per ogni livello.
     */
    public void aggiungiVita() {
        VITE++;
        vite++;
        setChanged();
        notifyObservers(Evento.VITE);
    }


    /**
     * Resetta lo stato di Link all'inizio di una nuova partita.
     */
    public void reset() {
        vita = HP_PER_VITA;
        vite = VITE;
        rupie = 0;
        carote = 0;
        cristalli = 0;
        bonusDanno = 0;
        scudoAttivo = false;
        attaccando = false;
        ultimoCristallo = 0;
        spada = new SpadaBase();
        scudo = new ScudoBase();
        riposiziona();
    }

    /**
     * Riporta Link al punto di partenza di un livello: un tile dal bordo
     * sinistro, sulla riga centrale della schermata.
     * Non tocca vita, vite e inventario, perché viene richiamato anche
     * quando si passa al livello successivo dopo lo shop.
     */
    public void riposiziona() {
        x = GameConstants.TILE_SIZE;
        y = GameConstants.TILE_SIZE * 5;
    }

    /**
     * Restituisce i punti vita attuali.
     *
     * @return i punti vita attuali
     */
    public int getVita() { return vita; }
    /**
     * Restituisce il numero di vite rimaste.
     *
     * @return il numero di vite rimaste
     */
    public int getVite() { return vite; }
    /**
     * Restituisce il numero di rupie possedute.
     *
     * @return il numero di rupie possedute
     */
    public int getRupie() { return rupie; }
    /**
     * Restituisce il numero di carote possedute.
     *
     * @return il numero di carote possedute
     */
    public int getCarote() { return carote; }
    /**
     * Restituisce il numero di cristalli raccolti.
     *
     * @return il numero di cristalli raccolti
     */
    public int getCristalli() { return cristalli; }
    /**
     * Restituisce la spada equipaggiata.
     *
     * @return la spada equipaggiata
     */
    public Spada getSpada() { return spada; }
    /**
     * Restituisce lo scudo equipaggiato.
     *
     * @return lo scudo equipaggiato
     */
    public Scudo getScudo() { return scudo; }
    /**
     * Restituisce il bonus di danno accumulato.
     *
     * @return il bonus di danno accumulato
     */
    public int getBonusDanno() { return bonusDanno; }
    /**
     * Indica se lo scudo e' attivo.
     *
     * @return true se lo scudo e' attivo
     */
    public boolean isScudoAttivo() { return scudoAttivo; }
    /**
     * Indica se sta attaccando.
     *
     * @return true se sta attaccando
     */
    public boolean isAttaccando() { return attaccando; }
  
}
