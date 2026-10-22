package Controller;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Gestisce gli input da tastiera del giocatore.
 * Tiene traccia dei tasti attualmente premuti.
 */
public class InputHandler implements KeyListener {

    /** Insieme dei codici dei tasti attualmente premuti. */
    private Set<Integer> tastiPremuti;

    /**
     * Costruttore di InputHandler.
     */
    public InputHandler() {
        tastiPremuti = new HashSet<>();
    }

    /**
     * Registra un tasto come premuto.
     *
     * @param e l'evento di pressione generato da Swing
     */
    @Override
    public void keyPressed(KeyEvent e) {
        tastiPremuti.add(e.getKeyCode());
    }

    /**
     * Toglie un tasto dall'insieme di quelli premuti.
     *
     * @param e l'evento di rilascio generato da Swing
     */
    @Override
    public void keyReleased(KeyEvent e) {
        tastiPremuti.remove(e.getKeyCode());
    }

    /**
     * Non usato: il gioco reagisce alla pressione e al rilascio dei tasti,
     * non al carattere digitato. Va comunque implementato perche' fa parte
     * dell'interfaccia KeyListener.
     *
     * @param e l'evento di digitazione generato da Swing
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Dimentica tutti i tasti risultanti premuti.
     * Serve ai cambi di schermata: se un tasto viene rilasciato mentre il
     * pannello di gioco non ha il fuoco, il suo keyReleased arriva a un altro
     * componente e quel tasto resterebbe premuto per sempre.
     */
    public void azzera() {
        tastiPremuti.clear();
    }

    /**
     * Controlla se un tasto è attualmente premuto.
     *
     * @param keyCode codice del tasto da controllare
     * @return true se il tasto è premuto
     */
    public boolean isPremuto(int keyCode) {
        return tastiPremuti.contains(keyCode);
    }

    /**
     * Indica se il tasto su e' premuto.
     *
     * @return true se il tasto su e' premuto
     */
    public boolean suPremuto()       { return isPremuto(KeyEvent.VK_UP); }
    /**
     * Indica se il tasto giu' e' premuto.
     *
     * @return true se il tasto giu' e' premuto
     */
    public boolean giuPremuto()      { return isPremuto(KeyEvent.VK_DOWN); }
    /**
     * Indica se il tasto sinistra e' premuto.
     *
     * @return true se il tasto sinistra e' premuto
     */
    public boolean sinistraPremuto() { return isPremuto(KeyEvent.VK_LEFT); }
    /**
     * Indica se il tasto destra e' premuto.
     *
     * @return true se il tasto destra e' premuto
     */
    public boolean destroPremuto()   { return isPremuto(KeyEvent.VK_RIGHT); }
    /**
     * Indica se il tasto di attacco e' premuto.
     *
     * @return true se il tasto di attacco e' premuto
     */
    public boolean attaccoPremuto()  { return isPremuto(KeyEvent.VK_SPACE); }
    /**
     * Indica se il tasto dello scudo e' premuto.
     * Si usa S e non AltGr perche' su Windows AltGr non arriva come tasto
     * a se': il sistema lo traduce in Ctrl + Alt, quindi VK_ALT_GRAPH non
     * viene mai ricevuto e lo scudo non si attiverebbe mai.
     *
     * @return true se il tasto dello scudo e' premuto
     */
    public boolean scudoPremuto()    { return isPremuto(KeyEvent.VK_S); }
    /**
     * Indica se il tasto della carota e' premuto.
     *
     * @return true se il tasto della carota e' premuto
     */
    public boolean carotaPremuta()   { return isPremuto(KeyEvent.VK_A); }
}