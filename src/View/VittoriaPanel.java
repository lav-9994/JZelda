package View;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello mostrato quando Link completa tutti gli 8 livelli.
 */
public class VittoriaPanel extends JPanel {

    /** Pulsante per tornare al menu principale. */
    private JButton btnMenu;

    /**
     * Costruttore di VittoriaPanel.
     */
    public VittoriaPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("HAI VINTO!", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 48));
        titolo.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        add(titolo, gbc);

        JLabel sottotitolo = new JLabel("Hai completato tutti gli 8 livelli!", SwingConstants.CENTER);
        sottotitolo.setForeground(Color.WHITE);
        sottotitolo.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        add(sottotitolo, gbc);

        btnMenu = new JButton("Menu");
        gbc.gridy = 2;
        add(btnMenu, gbc);
    }

    public JButton getBtnMenu() { return btnMenu; }
}