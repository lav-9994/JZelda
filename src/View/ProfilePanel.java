package View;

import Model.*;
import Model.Profilo.ProfiloEntry;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

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

    /** Menu a tendina per scegliere l'avatar di un nuovo profilo. */
    private JComboBox<String> sceltaAvatar;

    /** Mostra l'avatar del profilo selezionato nella lista. */
    private JLabel anteprimaAvatar;

    /** Dimensione a cui viene mostrata l'anteprima dell'avatar. */
    private static final int DIMENSIONE_ANTEPRIMA = 96;

    /**
     * Avatar disponibili: nome mostrato all'utente e file corrispondente.
     * Sono sprite gia' presenti fra le risorse del gioco.
     */
    private static final String[][] AVATAR_DISPONIBILI = {
        { "Link",       "link.png"       },
        { "Vespasiano", "vespasiano.png" },
        { "Nemico",     "nemico.png"     },
        { "Boss",       "boss.png"       },
    };

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

        sceltaAvatar = new JComboBox<>();
        for (String[] avatar : AVATAR_DISPONIBILI) sceltaAvatar.addItem(avatar[0]);
        sceltaAvatar.setToolTipText("Scegli l'avatar del nuovo profilo");
        gbc.gridy = 2;
        add(sceltaAvatar, gbc);

        btnCreaProfilo = new JButton("Crea Profilo");
        gbc.gridy = 3;
        add(btnCreaProfilo, gbc);

        modelloLista = new DefaultListModel<>();
        listaProfili = new JList<>(modelloLista);
        listaProfili.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(listaProfili);
        scroll.setPreferredSize(new Dimension(200, 150));
        gbc.gridy = 4;
        add(scroll, gbc);

        anteprimaAvatar = new JLabel("", SwingConstants.CENTER);
        anteprimaAvatar.setPreferredSize(new Dimension(DIMENSIONE_ANTEPRIMA, DIMENSIONE_ANTEPRIMA));
        gbc.gridy = 5;
        add(anteprimaAvatar, gbc);

        listaProfili.addListSelectionListener(this::mostraAvatarSelezionato);

        btnSeleziona = new JButton("Seleziona");
        gbc.gridy = 6;
        add(btnSeleziona, gbc);

        btnIndietro = new JButton("Indietro");
        gbc.gridy = 7;
        add(btnIndietro, gbc);

        aggiorna();
    }

    /**
     * Mostra nell'anteprima l'avatar del profilo selezionato nella lista.
     *
     * @param evento l'evento di cambio selezione
     */
    private void mostraAvatarSelezionato(ListSelectionEvent evento) {
        if (evento.getValueIsAdjusting()) return;
        int indice = listaProfili.getSelectedIndex();
        if (indice == -1) {
            anteprimaAvatar.setIcon(null);
            return;
        }
        anteprimaAvatar.setIcon(caricaAvatar(profilo.getProfili().get(indice).getAvatar()));
    }

    /**
     * Carica l'immagine di un avatar e la ridimensiona per l'anteprima.
     *
     * @param nomeFile nome del file dell'avatar, eventualmente vuoto
     * @return l'icona da mostrare, o null se il file manca o non e' indicato
     */
    private ImageIcon caricaAvatar(String nomeFile) {
        if (nomeFile == null || nomeFile.isEmpty()) return null;
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/sprites/" + nomeFile));
            return new ImageIcon(img.getScaledInstance(
                DIMENSIONE_ANTEPRIMA, DIMENSIONE_ANTEPRIMA, Image.SCALE_FAST));
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Restituisce il file dell'avatar scelto nel menu a tendina.
     *
     * @return nome del file dell'avatar da salvare nel profilo
     */
    public String getAvatarScelto() {
        int indice = sceltaAvatar.getSelectedIndex();
        if (indice < 0) return "";
        return AVATAR_DISPONIBILI[indice][1];
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

    /**
     * Restituisce il nickname digitato nel campo di testo.
     *
     * @return il nickname digitato nel campo di testo
     */
    public String getNickname() { return txtNickname.getText().trim(); }
    /**
     * Restituisce il pulsante Crea Profilo.
     *
     * @return il pulsante Crea Profilo
     */
    public JButton getBtnCreaProfilo() { return btnCreaProfilo; }
    /**
     * Restituisce il pulsante Seleziona.
     *
     * @return il pulsante Seleziona
     */
    public JButton getBtnSeleziona() { return btnSeleziona; }
    /**
     * Restituisce il pulsante Indietro.
     *
     * @return il pulsante Indietro
     */
    public JButton getBtnIndietro() { return btnIndietro; }
}