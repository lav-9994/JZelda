package View;
import javax.swing.*;
import java.awt.*;
/**
 * Pannello del menu principale.
 * Mostra i pulsanti per iniziare la partita, vedere il profilo e la classifica.
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

    /**
     * Costruttore di MenuPanel.
     * Inizializza e dispone i componenti grafici.
     */
    public MenuPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("JZelda", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 48));
        titolo.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        add(titolo, gbc);

        btnGioca = new JButton("Gioca");
        btnProfilo = new JButton("Profilo");
        btnClassifica = new JButton("Classifica");
        btnEsci = new JButton("Esci");

        gbc.gridy = 1; add(btnGioca, gbc);
        gbc.gridy = 2; add(btnProfilo, gbc);
        gbc.gridy = 3; add(btnClassifica, gbc);
        gbc.gridy = 4; add(btnEsci, gbc);
    }

    public JButton getBtnGioca() { return btnGioca; }
    public JButton getBtnProfilo() { return btnProfilo; }
    public JButton getBtnClassifica() { return btnClassifica; }
    public JButton getBtnEsci() { return btnEsci; }
}