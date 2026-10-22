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
     * Solo pochi eventi richiedono di cambiare pannello o di modificare una  schermata: 
     * tutti gli altri, come il movimento o la vita che cala, traducono in un ridisegno del gioco, ed e' quello che fa l'ultimo ramo.
     * Alla schermata di vittoria non si arriva da qui ma dal Controller, che la mostra direttamente quando finisce l'ottavo livello.
     *
     * @param o   il Model che ha notificato
     * @param arg l'evento, una costante di Evento
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Evento evento) {
            switch (evento) {
                case GAME_OVER            -> mostraPanel("GAME_OVER");
                case RUPIE                -> shopPanel.aggiorna();
                case PROFILO, STATISTICHE -> {
                    profilePanel.aggiorna();
                    leaderboardPanel.aggiorna();
                }
                default                   -> gamePanel.repaint();
            }
        }
    }

    /**
     * Restituisce il pannello di gioco.
     *
     * @return il pannello di gioco
     */
    public GamePanel getGamePanel() { return gamePanel; }
    /**
     * Restituisce il pannello dello shop.
     *
     * @return il pannello dello shop
     */
    public ShopPanel getShopPanel() { return shopPanel; }
    /**
     * Restituisce il pannello dei profili.
     *
     * @return il pannello dei profili
     */
    public ProfilePanel getProfilePanel() { return profilePanel; }
    /**
     * Restituisce il pannello della classifica.
     *
     * @return il pannello della classifica
     */
    public LeaderboardPanel getLeaderboardPanel() { return leaderboardPanel; }
    /**
     * Restituisce il pannello di game over.
     *
     * @return il pannello di game over
     */
    public GameOverPanel getGameOverPanel() { return gameOverPanel; }
    /**
     * Restituisce il pannello del menu.
     *
     * @return il pannello del menu
     */
    public MenuPanel getMenuPanel() { return menuPanel; }
    /**
     * Restituisce il pannello della vittoria.
     *
     * @return il pannello della vittoria
     */
    public VittoriaPanel getVittoriaPanel() { return vittoriaPanel; }
}