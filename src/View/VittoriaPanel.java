package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Pannello mostrato quando Link completa tutti gli 8 livelli.
 * La schermata e' un'immagine a tutto pannello; il pulsante sta sopra.
 */
public class VittoriaPanel extends JPanel {

    /** Pulsante per tornare al menu principale. */
    private JButton btnMenu;

    /** Immagine di vittoria, usata come sfondo del pannello. */
    private BufferedImage sfondo;

    /**
     * Quota dello spazio libero lasciata sopra il pulsante: alzandola scende,
     * abbassandola sale. Regolata per metterlo sopra le montagne.
     */
    private static final double PESO_SOPRA = 0.80;

    /**
     * Costruttore di VittoriaPanel.
     * Titolo e sottotitolo non servono come etichette: la scritta e' gia'
     * nell'immagine.
     */
    public VittoriaPanel() {
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

        btnMenu = new JButton("Menu");
        gbc.gridy = 1;
        add(btnMenu, gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0 - PESO_SOPRA;
        add(Box.createVerticalGlue(), gbc);
    }

    /**
     * Carica la schermata di vittoria dal classpath.
     *
     * @return l'immagine, o null se non e' stata trovata
     */
    private BufferedImage caricaSfondo() {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/sprites/vittoria.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Schermata di vittoria non trovata");
            return null;
        }
    }

    /**
     * Disegna la schermata di vittoria come sfondo, sotto al pulsante.
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
     * Restituisce il pulsante Menu.
     *
     * @return il pulsante Menu
     */
    public JButton getBtnMenu() { return btnMenu; }
}
