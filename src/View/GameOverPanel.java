package View;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello mostrato quando Link esaurisce tutte le vite.
 */
public class GameOverPanel extends JPanel {

    /** Pulsante per ricominciare una nuova partita. */
    private JButton btnRigioca;

    /** Pulsante per tornare al menu principale. */
    private JButton btnMenu;

    /**
     * Costruttore di GameOverPanel.
     */
    public GameOverPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("GAME OVER", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 48));
        titolo.setForeground(Color.RED);
        gbc.gridy = 0;
        add(titolo, gbc);

        btnRigioca = new JButton("Rigioca");
        gbc.gridy = 1;
        add(btnRigioca, gbc);

        btnMenu = new JButton("Menu");
        gbc.gridy = 2;
        add(btnMenu, gbc);
    }

    public JButton getBtnRigioca() { return btnRigioca; }
    public JButton getBtnMenu() { return btnMenu; }
}