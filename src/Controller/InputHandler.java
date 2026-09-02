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

    @Override
    public void keyPressed(KeyEvent e) {
        tastiPremuti.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        tastiPremuti.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Controlla se un tasto è attualmente premuto.
     *
     * @param keyCode codice del tasto da controllare
     * @return true se il tasto è premuto
     */
    public boolean isPremuto(int keyCode) {
        return tastiPremuti.contains(keyCode);
    }

    public boolean suPremuto()       { return isPremuto(KeyEvent.VK_UP); }
    public boolean giuPremuto()      { return isPremuto(KeyEvent.VK_DOWN); }
    public boolean sinistraPremuto() { return isPremuto(KeyEvent.VK_LEFT); }
    public boolean destroPremuto()   { return isPremuto(KeyEvent.VK_RIGHT); }
    public boolean attaccoPremuto()  { return isPremuto(KeyEvent.VK_SPACE); }
    public boolean scudoPremuto()    { return isPremuto(KeyEvent.VK_ALT_GRAPH); }
    public boolean carotaPremuta()   { return isPremuto(KeyEvent.VK_A); }
}