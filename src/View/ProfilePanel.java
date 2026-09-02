package View;

import Model.*;
import Model.Profilo.ProfiloEntry;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Pannello per la gestione dei profili utente.
 * Permette di creare un nuovo profilo e selezionarne uno esistente.
 */
public class ProfilePanel extends JPanel {

    /** Campo di testo per inserire il nickname. */
    private JTextField txtNickname;

    /** Pulsante per creare un nuovo profilo. */
    private JButton btnCreaProfilo;

    /** Lista dei profili esistenti. */
    private JList<String> listaProfili;

    /** Modello della lista dei profili. */
    private DefaultListModel<String> modelloLista;

    /** Pulsante per selezionare il profilo scelto. */
    private JButton btnSeleziona;

    /** Pulsante per tornare al menu. */
    private JButton btnIndietro;

    /** Riferimento al profilo. */
    private Profilo profilo;

    /**
     * Costruttore di ProfilePanel.
     *
     * @param profilo il model dei profili
     */
    public ProfilePanel(Profilo profilo) {
        this.profilo = profilo;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("Profilo", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 32));
        titolo.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        add(titolo, gbc);

        txtNickname = new JTextField(15);
        txtNickname.setToolTipText("Inserisci nickname");
        gbc.gridy = 1;
        add(txtNickname, gbc);

        btnCreaProfilo = new JButton("Crea Profilo");
        gbc.gridy = 2;
        add(btnCreaProfilo, gbc);

        modelloLista = new DefaultListModel<>();
        listaProfili = new JList<>(modelloLista);
        listaProfili.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(listaProfili);
        scroll.setPreferredSize(new Dimension(200, 150));
        gbc.gridy = 3;
        add(scroll, gbc);

        btnSeleziona = new JButton("Seleziona");
        gbc.gridy = 4;
        add(btnSeleziona, gbc);

        btnIndietro = new JButton("Indietro");
        gbc.gridy = 5;
        add(btnIndietro, gbc);

        aggiorna();
    }

    /**
     * Aggiorna la lista dei profili dal CSV.
     */
    public void aggiorna() {
        modelloLista.clear();
        List<ProfiloEntry> profili = profilo.getProfili();
        for (ProfiloEntry p : profili) {
            modelloLista.addElement(p.getNickname() + " — V:" + p.getVinte() + " P:" + p.getPerse());
        }
    }

    /**
     * Restituisce il nickname selezionato dalla lista.
     *
     * @return nickname selezionato o null
     */
    public String getNicknameSelezionato() {
        int index = listaProfili.getSelectedIndex();
        if (index == -1) return null;
        return profilo.getProfili().get(index).getNickname();
    }

    public String getNickname() { return txtNickname.getText().trim(); }
    public JButton getBtnCreaProfilo() { return btnCreaProfilo; }
    public JButton getBtnSeleziona() { return btnSeleziona; }
    public JButton getBtnIndietro() { return btnIndietro; }
}