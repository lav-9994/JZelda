package View;

import Model.*;
import javax.swing.*;
import java.awt.*;

/**
 * Pannello dello shop.
 * Mostra gli item disponibili e permette a Link di acquistarli.
 */
public class ShopPanel extends JPanel {

    /** Etichetta che mostra le rupie correnti di Link. */
    private JLabel lblRupie;

    /** Pulsante per acquistare una carota. */
    private JButton btnAcquistaCarota;

    /** Pulsante per continuare al livello successivo. */
    private JButton btnContinua;

    /** Riferimento a Link per mostrare le rupie. */
    private Link link;

    /**
     * Costruttore di ShopPanel.
     *
     * @param link il personaggio principale
     */
    public ShopPanel(Link link) {
        this.link = link;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("Shop", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 32));
        titolo.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        add(titolo, gbc);

        lblRupie = new JLabel("Rupie: " + link.getRupie(), SwingConstants.CENTER);
        lblRupie.setForeground(Color.WHITE);
        lblRupie.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        add(lblRupie, gbc);

        btnAcquistaCarota = new JButton("Carota — 5 Rupie");
        gbc.gridy = 2;
        add(btnAcquistaCarota, gbc);

        btnContinua = new JButton("Continua");
        gbc.gridy = 3;
        add(btnContinua, gbc);
    }

    /**
     * Aggiorna il pannello con i dati aggiornati di Link.
     */
    public void aggiorna() {
        lblRupie.setText("Rupie: " + link.getRupie());
    }

    public JButton getBtnAcquistaCarota() { return btnAcquistaCarota; }
    public JButton getBtnContinua() { return btnContinua; }
}