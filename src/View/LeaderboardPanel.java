package View;

import Model.*;
import Model.Profilo.ProfiloEntry;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Pannello della classifica.
 * Mostra i profili ordinati per partite vinte.
 */
public class LeaderboardPanel extends JPanel {

    /** Modello della lista della classifica. */
    private DefaultListModel<String> modelloLista;

    /** Lista della classifica. */
    private JList<String> listaClassifica;

    /** Pulsante per tornare al menu. */
    private JButton btnIndietro;

    /** Riferimento al profilo per leggere i dati. */
    private Profilo profilo;

    /**
     * Costruttore di LeaderboardPanel.
     *
     * @param profilo il model dei profili
     */
    public LeaderboardPanel(Profilo profilo) {
        this.profilo = profilo;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("Classifica", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 32));
        titolo.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        add(titolo, gbc);

        modelloLista = new DefaultListModel<>();
        listaClassifica = new JList<>(modelloLista);
        listaClassifica.setBackground(Color.DARK_GRAY);
        listaClassifica.setForeground(Color.WHITE);
        listaClassifica.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scroll = new JScrollPane(listaClassifica);
        scroll.setPreferredSize(new Dimension(300, 200));
        gbc.gridy = 1;
        add(scroll, gbc);

        btnIndietro = new JButton("Indietro");
        gbc.gridy = 2;
        add(btnIndietro, gbc);

        aggiorna();
    }

    /**
     * Aggiorna la classifica leggendo i profili ordinati.
     */
    public void aggiorna() {
        modelloLista.clear();
        List<ProfiloEntry> classifica = profilo.getProfiliOrdinati();
        for (int i = 0; i < classifica.size(); i++) {
            ProfiloEntry p = classifica.get(i);
            modelloLista.addElement((i + 1) + ". " + p.getNickname() + " — Vittorie: " + p.getVinte());
        }
    }

    /**
     * Restituisce il pulsante Indietro.
     *
     * @return il pulsante Indietro
     */
    public JButton getBtnIndietro() { return btnIndietro; }
}