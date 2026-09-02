package Model;
import java.util.Random;
import Config.GameConstants;
/**
 * Classe che rappresenta un coniglietto spawnato alla morte di un boss.
 * Un solo coniglietto per gruppo è speciale.
 * Il coniglietto speciale si avvicina a Link invece di scappare.
 * Se Link ha carote le mangia: 50% ripristina vita al massimo, 50% infligge danno 2.
 * Se Link non ha carote infligge danno 2.
 */
public class Coniglietti extends Personaggio {
    
    /** Indica se il coniglietto è speciale. */
    private boolean speciale;
    
    /** Indica se il coniglietto è ancora attivo nella stanza. */
    private boolean attivo;
    
    /**
     * Costruttore di Coniglietti.
     * Determina casualmente se il coniglietto è speciale.
     *
     * @param x posizione iniziale x
     * @param y posizione iniziale y
     */
    
    public Coniglietti(int x, int y) {
        super(x, y, 1, 0, 16, 16);
        this.speciale = false;
        this.attivo = true;
        this.direzione = Direzione.DESTRA;
    }
    
    /**
     * Movimento base — delegato ai metodi specifici dal Controller.
     */
    @Override
    public void muovi() {
    }
    /**
     * Muove il coniglietto lontano da Link (coniglietto normale).
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    public void muoviLontanoDa(int linkX, int linkY) {
        if (x < linkX) x -= GameConstants.VELOCITA_CONIGLIO;
        else x += GameConstants.VELOCITA_CONIGLIO;
        if (y < linkY) y -= GameConstants.VELOCITA_CONIGLIO;
        else y += GameConstants.VELOCITA_CONIGLIO;
    }
    /**
     * Muove il coniglietto verso Link (coniglietto speciale).
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    
    public void muoviVerso(int linkX, int linkY) {
        if (x < linkX) x += GameConstants.VELOCITA_CONIGLIO;
        else x -= GameConstants.VELOCITA_CONIGLIO;
        if (y < linkY) y += GameConstants.VELOCITA_CONIGLIO;
        else y -= GameConstants.VELOCITA_CONIGLIO;
    }
    
    /**
     * Gestisce l'interazione tra il coniglietto speciale e Link.
     * Se Link ha carote le mangia: 50% ripristina vita al massimo, 50% infligge danno 2.
     * Se Link non ha carote infligge danno 2.
     *
     * @param link il personaggio principale
     */
    
    public void interagisciConLink(Link link) {
        if (!speciale) return;
        Random random = new Random();
        if (link.getCarote() > 0) {
            link.rimuoviCarota();
            if (random.nextInt(2) == 0) {
                link.aggiungiVita();
                setChanged();
                notifyObservers("vita_bonus");
            }
               
             else {
                link.subisciDanno(GameConstants.DANNO_CONIGLIO);
                setChanged();
                notifyObservers("danno_coniglio");
            }
        } else {
            link.subisciDanno(GameConstants.DANNO_CONIGLIO);
            setChanged();
            notifyObservers("danno_coniglio");
        }
        attivo = false;
        setChanged();
        notifyObservers("disattivato");
    }
    /**
     * Disattiva il coniglietto quando esce dalla stanza.
     */
    public void disattiva() {
        attivo = false;
        setChanged();
        notifyObservers("disattivato");
    }
    
    public boolean isSpeciale() { return speciale; }
    public boolean isAttivo() { return attivo; }
    public void setSpeciale(boolean speciale) { this.speciale = speciale; }
}

