package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Finestra principale del gioco.
 * Gestisce il cambio tra i pannelli tramite CardLayout.
 * Implementa Observer per reagire ai cambiamenti del Model.
 */
public class GameView extends JFrame implements Observer {

    /** Layout che permette di cambiare pannello. */
    private CardLayout cardLayout;

    /** Pannello contenitore principale. */
    private JPanel contenitore;

    /** Pannello del menu principale. */
    private MenuPanel menuPanel;

    /** Pannello di gioco principale. */
    private GamePanel gamePanel;

    /** Pannello dello shop. */
    private ShopPanel shopPanel;

    /** Pannello del profilo. */
    private ProfilePanel profilePanel;

    /** Pannello della classifica. */
    private LeaderboardPanel leaderboardPanel;

    /** Pannello di game over. */
    private GameOverPanel gameOverPanel;

    /** Pannello di vittoria. */
    private VittoriaPanel vittoriaPanel;

    /**
     * Costruttore di GameView.
     *
     * @param link    il personaggio principale
     * @param profilo il profilo utente corrente
     */
    public GameView(Link link, Profilo profilo) {
        link.addObserver(this);
        profilo.addObserver(this);

        setTitle("JZelda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        contenitore = new JPanel(cardLayout);

        menuPanel = new MenuPanel();
        gamePanel = new GamePanel(link);
        shopPanel = new ShopPanel(link);
        profilePanel = new ProfilePanel(profilo);
        leaderboardPanel = new LeaderboardPanel(profilo);
        gameOverPanel = new GameOverPanel();
        vittoriaPanel = new VittoriaPanel();

        contenitore.add(menuPanel, "MENU");
        contenitore.add(gamePanel, "GIOCO");
        contenitore.add(shopPanel, "SHOP");
        contenitore.add(profilePanel, "PROFILO");
        contenitore.add(leaderboardPanel, "CLASSIFICA");
        contenitore.add(gameOverPanel, "GAME_OVER");
        contenitore.add(vittoriaPanel, "VITTORIA");

        add(contenitore);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        mostraPanel("MENU");
    }

    /**
     * Aggiorna il livello corrente nel GamePanel.
     *
     * @param livello il livello corrente
     */
    public void setLivello(Livello livello) {
        gamePanel.setLivello(livello);
    }

    /**
     * Mostra il pannello corrispondente al nome dato.
     *
     * @param nome nome del pannello da mostrare
     */
    public void mostraPanel(String nome) {
        cardLayout.show(contenitore, nome);
    }

    /**
     * Riceve le notifiche dal Model e aggiorna la View di conseguenza.
     *
     * @param o   il Model che ha notificato
     * @param arg stringa che identifica il tipo di aggiornamento
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            switch ((String) arg) {
                case "game_over"               -> mostraPanel("GAME_OVER");
                case "vittoria"                -> mostraPanel("VITTORIA");
                case "rupie"                   -> shopPanel.aggiorna();
                case "profilo", "statistiche"  -> {
                    profilePanel.aggiorna();
                    leaderboardPanel.aggiorna();
                }
                default                        -> gamePanel.repaint();
            }
        }
    }

    public GamePanel getGamePanel() { return gamePanel; }
    public ShopPanel getShopPanel() { return shopPanel; }
    public ProfilePanel getProfilePanel() { return profilePanel; }
    public LeaderboardPanel getLeaderboardPanel() { return leaderboardPanel; }
    public GameOverPanel getGameOverPanel() { return gameOverPanel; }
    public MenuPanel getMenuPanel() { return menuPanel; }
    public VittoriaPanel getVittoriaPanel() { return vittoriaPanel; }
}