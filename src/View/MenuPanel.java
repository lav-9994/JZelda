package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Pannello del menu principale.
 * All'avvio mostra solo la schermata di titolo: i pulsanti compaiono
 * sopra l'immagine al primo clic.
 */
public class MenuPanel extends JPanel {

    /** Pulsante per iniziare una nuova partita. */
    private JButton btnGioca;

    /** Pulsante per accedere al profilo. */
    private JButton btnProfilo;

    /** Pulsante per vedere la classifica. */
    private JButton btnClassifica;

    /** Pulsante per uscire dal gioco. */
    private JButton btnEsci;

    /** Immagine di titolo, usata come sfondo del menu. */
    private BufferedImage sfondo;

    /**
     * Quota dello spazio libero lasciata sopra i pulsanti: alzandola i pulsanti
     * scendono, abbassandola salgono. Regolata per centrarli sullo stemma.
     */
    private static final double PESO_SOPRA = 0.83;

    /**
     * Costruttore di MenuPanel.
     * Carica la schermata di titolo, prepara i pulsanti nascosti e resta
     * in attesa del clic che li fara' comparire.
     */
    public MenuPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        sfondo = caricaSfondo();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // due spaziatori elastici sopra e sotto tengono il blocco dei pulsanti
        // sullo stemma: lo spazio libero viene diviso fra loro in proporzione
        gbc.gridy = 0;
        gbc.weighty = PESO_SOPRA;
        add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0;

        btnGioca = new JButton("Gioca");
        btnProfilo = new JButton("Profilo");
        btnClassifica = new JButton("Classifica");
        btnEsci = new JButton("Esci");

        gbc.gridy = 1; add(btnGioca, gbc);
        gbc.gridy = 2; add(btnProfilo, gbc);
        gbc.gridy = 3; add(btnClassifica, gbc);
        gbc.gridy = 4; add(btnEsci, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0 - PESO_SOPRA;
        add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0;

        nascondiPulsanti();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostraPulsanti();
            }
        });
    }

    /**
     * Carica la schermata di titolo dal classpath.
     *
     * @return l'immagine di titolo, o null se non e' stata trovata
     */
    private BufferedImage caricaSfondo() {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/sprites/schermata iniziale.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Schermata iniziale non trovata");
            return null;
        }
    }

    /**
     * Nasconde i pulsanti, lasciando visibile la sola schermata di titolo.
     */
    private void nascondiPulsanti() {
        btnGioca.setVisible(false);
        btnProfilo.setVisible(false);
        btnClassifica.setVisible(false);
        btnEsci.setVisible(false);
    }

    /**
     * Fa comparire i pulsanti sopra la schermata di titolo.
     * Chiamato al primo clic sul pannello.
     */
    private void mostraPulsanti() {
        btnGioca.setVisible(true);
        btnProfilo.setVisible(true);
        btnClassifica.setVisible(true);
        btnEsci.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Disegna la schermata di titolo come sfondo, sotto ai pulsanti.
     *
     * @param g contesto grafico
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, getWidth(), getHeight(), null);
        }
    }

    /**
     * Restituisce il pulsante Gioca.
     *
     * @return il pulsante Gioca
     */
    public JButton getBtnGioca() { return btnGioca; }
    /**
     * Restituisce il pulsante Profilo.
     *
     * @return il pulsante Profilo
     */
    public JButton getBtnProfilo() { return btnProfilo; }
    /**
     * Restituisce il pulsante Classifica.
     *
     * @return il pulsante Classifica
     */
    public JButton getBtnClassifica() { return btnClassifica; }
    /**
     * Restituisce il pulsante Esci.
     *
     * @return il pulsante Esci
     */
    public JButton getBtnEsci() { return btnEsci; }
}