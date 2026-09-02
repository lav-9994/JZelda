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

    public Link(int x, int y) {
        super(x, y, HP_PER_VITA, 1, 16, 16);
        this.vite = VITE;
        this.spada = new SpadaBase();
        this.bonusDanno = 0;
        this.rupie = 0;
        this.carote = 0;
        this.scudoAttivo = false;
        this.attaccando = false;
    }
    
    /**
     * Muove Link nella direzione corrente di una quantità fissa di pixel.
     * Notifica gli observer del movimento.
     */
    @Override
    public void muovi() {
        switch (direzione) {
            case SU:    y -= GameConstants.VELOCITA_LINK; break;
            case GIU:   y += GameConstants.VELOCITA_LINK; break;
            case SINISTRA: x -= GameConstants.VELOCITA_LINK; break;
            case DESTRA:   x += GameConstants.VELOCITA_LINK; break;
        }
        setChanged();
        notifyObservers("movimento");
    }

    /**
     * Link attacca nella direzione corrente.
     * Notifica gli observer dell'attacco.
     */
    public void attacca() {
        attaccando = true;
        setChanged();
        notifyObservers("attacco");
    }

    /**
     * Termina l'animazione di attacco.
     */
    public void terminaAttacco() {
        attaccando = false;
    }

    /**
     * Attiva lo scudo di Link.
     * Notifica gli observer.
     */
    public void attivaScudo() {
        scudoAttivo = true;
        setChanged();
        notifyObservers("scudo");
    }

    /**
     * Disattiva lo scudo.
     */
    public void disattivaScudo() {
        scudoAttivo = false;
        setChanged(); 
        notifyObservers("scudo");
    }

    /**
     * Link subisce danno. Se lo scudo è attivo il danno è dimezzato.
     * Link è invulnerabile per 1.5 secondi dopo aver ricevuto un colpo.
     * @param danno quantità di danno subito
     */
    @Override
    public void subisciDanno(int danno) {
        if (System.currentTimeMillis() - ultimoDanno < COOLDOWN_DANNO) return;
        ultimoDanno = System.currentTimeMillis();
        if (scudoAttivo) danno /= 2;
        super.subisciDanno(danno);
        if (!isVivo()) {
            vite--;
            if (vite <= 0) {
                setChanged();
                notifyObservers("game_over");
            } else {
                vita = HP_PER_VITA;
                setChanged();
                notifyObservers("vita");
            }
        } else {
            setChanged();
            notifyObservers("vita");
        }
    }
    /**
     * Link subisce danno da proiettile.
     * Se lo scudo è attivo il danno è nullo.
     *
     * @param danno quantità di danno subito
     */
    public void subisciDannoProiettile(int danno) {
        if (System.currentTimeMillis() - ultimoDanno < COOLDOWN_DANNO) return;
        ultimoDanno = System.currentTimeMillis();
        if (scudoAttivo) return;
        super.subisciDanno(danno);
        if (!isVivo()) {
            vite--;
            if (vite <= 0) {
                setChanged();
                notifyObservers("game_over");
            } else {
                vita = HP_PER_VITA;
                setChanged();
                notifyObservers("vita");
            }
        } else {
            setChanged();
            notifyObservers("vita");
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
        notifyObservers("spada");
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
        notifyObservers("danno");
    }
    public void aggiungiCristallo() {
        cristalli++;
        setChanged();
        notifyObservers("cristallo");
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
            notifyObservers("vita");
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
        notifyObservers("rupie");
    }

    /**
     * Rimuove rupie dall'inventario di Link.
     *
     * @param quantita numero di rupie da rimuovere
     */
    public void rimuoviRupie(int quantita) {
        this.rupie = Math.max(0, this.rupie - quantita);
        setChanged();
        notifyObservers("rupie");
    }

    /**
     * Aggiunge carote all'inventario.
     *
     * @param quantita numero di carote da aggiungere
     */
    public void aggiungiCarote(int quantita) {
        this.carote += quantita;
        setChanged();
        notifyObservers("carote");
    }

    /**
     * Rimuove una carota dall'inventario.
     * Usato quando un coniglio mangia la carota.
     */
    public void rimuoviCarota() {
        if (carote > 0) carote--;
        setChanged();
        notifyObservers("carote");
    }

   
    /**
     * Aggiunge una vita a Link per ogni livello.
     */
    public void aggiungiVita() {
        VITE++;
        vite++;
        setChanged();
        notifyObservers("vite");
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
        spada = new SpadaBase();
        x = GameConstants.SCHERMATA_W / 2;
        y = GameConstants.SCHERMATA_H / 2;
    }

    // Getter e setter
    public int getVita() { return vita; }
    public int getVite() { return vite; }
    public int getRupie() { return rupie; }
    public int getCarote() { return carote; }
    public int getCristalli() { return cristalli; }
    public Spada getSpada() { return spada; }
    public int getBonusDanno() { return bonusDanno; }
    public boolean isScudoAttivo() { return scudoAttivo; }
    public boolean isAttaccando() { return attaccando; }
  
}
