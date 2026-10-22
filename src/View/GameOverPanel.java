package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Pannello mostrato quando Link esaurisce tutte le vite.
 * La schermata e' un'immagine a tutto pannello; i pulsanti stanno sopra.
 */
public class GameOverPanel extends JPanel {

    /** Pulsante per ricominciare una nuova partita. */
    private JButton btnRigioca;

    /** Pulsante per tornare al menu principale. */
    private JButton btnMenu;

    /** Immagine di sconfitta, usata come sfondo del pannello. */
    private BufferedImage sfondo;

    /**
     * Quota dello spazio libero lasciata sopra i pulsanti: alzandola i pulsanti
     * scendono, abbassandola salgono. Regolata per metterli nella fascia
     * libera sotto i teschi.
     */
    private static final double PESO_SOPRA = 0.86;

    /**
     * Costruttore di GameOverPanel.
     * Il titolo non serve come etichetta: la scritta e' gia' nell'immagine.
     */
    public GameOverPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        sfondo = caricaSfondo();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        gbc.gridy = 0;
        gbc.weighty = PESO_SOPRA;
        add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0;

        btnRigioca = new JButton("Rigioca");
        gbc.gridy = 1;
        add(btnRigioca, gbc);

        btnMenu = new JButton("Menu");
        gbc.gridy = 2;
        add(btnMenu, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0 - PESO_SOPRA;
        add(Box.createVerticalGlue(), gbc);
    }

    /**
     * Carica la schermata di sconfitta dal classpath.
     *
     * @return l'immagine, o null se non e' stata trovata
     */
    private BufferedImage caricaSfondo() {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/sprites/gameover.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Schermata di game over non trovata");
            return null;
        }
    }

    /**
     * Disegna la schermata di sconfitta come sfondo, sotto ai pulsanti.
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
     * Restituisce il pulsante Rigioca.
     *
     * @return il pulsante Rigioca
     */
    public JButton getBtnRigioca() { return btnRigioca; }
    /**
     * Restituisce il pulsante Menu.
     *
     * @return il pulsante Menu
     */
    public JButton getBtnMenu() { return btnMenu; }
}
