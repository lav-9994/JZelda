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

    /** True mentre il coniglietto compie un balzo, false durante la sosta. */
    private boolean saltando;

    /** Frame trascorsi nella fase corrente del saltello. */
    private int contatoreSalto;

    /** Durata di un balzo in frame. */
    private static final int FRAME_SALTO = 12;

    /** Durata della sosta fra un balzo e l'altro in frame. */
    private static final int FRAME_PAUSA = 30;

    /** Componente orizzontale della direzione di fuga: -1, 0 o 1. */
    private int fugaX;

    /** Componente verticale della direzione di fuga: -1, 0 o 1. */
    private int fugaY;

    /** Indica se la direzione di fuga e' gia' stata scelta. */
    private boolean fugaDecisa;

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

        Random random = new Random();
        this.saltando = random.nextBoolean();
        this.contatoreSalto = random.nextInt(saltando ? FRAME_SALTO : FRAME_PAUSA);

        this.fugaDecisa = false;
    }

    /**
     * Sceglie la direzione di fuga del coniglietto.
     * Parte dal verso opposto a Link e ne tiene per ognuno solo una componente scelta a caso, cosi' il gruppo si apre a ventaglio invece di muoversi compatto.
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    private void decidiFuga(int linkX, int linkY) {
        int viaX = (x < linkX) ? -1 : 1;
        int viaY = (y < linkY) ? -1 : 1;

        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                fugaX = viaX;
                fugaY = viaY;
                break;
            case 1:
                fugaX = viaX;
                fugaY = 0;
                break;
            default:
                fugaX = 0;
                fugaY = viaY;
                break;
        }
        fugaDecisa = true;
    }

    /**
     * Fa avanzare il ritmo del saltello, alternando balzo e sosta.
     *
     * @return true se in questo frame il coniglietto si sposta
     */
    private boolean avanzaSalto() {
        contatoreSalto++;
        int durata = saltando ? FRAME_SALTO : FRAME_PAUSA;
        if (contatoreSalto >= durata) {
            contatoreSalto = 0;
            saltando = !saltando;
        }
        return saltando;
    }
    
    /**
     * Movimento base, delegato ai metodi specifici dal Controller.
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
        if (!fugaDecisa) decidiFuga(linkX, linkY);

        if (fugaX != 0) direzione = (fugaX < 0) ? Direzione.SINISTRA : Direzione.DESTRA;
        if (!avanzaSalto()) return;

        x += fugaX * GameConstants.VELOCITA_CONIGLIO;
        y += fugaY * GameConstants.VELOCITA_CONIGLIO;
    }
    /**
     * Muove il coniglietto verso Link (coniglietto speciale).
     *
     * @param linkX posizione x di Link
     * @param linkY posizione y di Link
     */
    
    public void muoviVerso(int linkX, int linkY) {
        direzione = (x < linkX) ? Direzione.DESTRA : Direzione.SINISTRA;
        if (!avanzaSalto()) return;

        if (x < linkX) x += GameConstants.VELOCITA_CONIGLIO;
        else           x -= GameConstants.VELOCITA_CONIGLIO;
        if (y < linkY) y += GameConstants.VELOCITA_CONIGLIO;
        else           y -= GameConstants.VELOCITA_CONIGLIO;
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
                notifyObservers(Evento.VITA_BONUS);
            }
               
             else {
                link.subisciDanno(GameConstants.DANNO_CONIGLIO);
                setChanged();
                notifyObservers(Evento.DANNO_CONIGLIO);
            }
        } else {
            link.subisciDanno(GameConstants.DANNO_CONIGLIO);
            setChanged();
            notifyObservers(Evento.DANNO_CONIGLIO);
        }
        attivo = false;
        setChanged();
        notifyObservers(Evento.CONIGLIO_DISATTIVATO);
    }
    /**
     * Disattiva il coniglietto quando esce dalla stanza.
     */
    public void disattiva() {
        attivo = false;
        setChanged();
        notifyObservers(Evento.CONIGLIO_DISATTIVATO);
    }
    
    /**
     * Indica se e' il coniglietto speciale.
     *
     * @return true se e' il coniglietto speciale
     */
    public boolean isSpeciale() { return speciale; }
    /**
     * Indica se e' ancora attivo.
     *
     * @return true se e' ancora attivo
     */
    public boolean isAttivo() { return attivo; }
    /**
     * Segna questo coniglietto come speciale.
     *
     * @param speciale il nuovo valore
     */
    public void setSpeciale(boolean speciale) { this.speciale = speciale; }
}

