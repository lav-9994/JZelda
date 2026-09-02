package Controller;

import Model.*;
import View.*;

/**
 * Classe principale del gioco JZelda.
 * Contiene il metodo main che avvia il gioco.
 */
public class JZelda {

    /**
     * Punto di ingresso del programma.
     * Inizializza Model, View e Controller e avvia il gioco.
     *
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
    	System.out.println(new java.io.File("res/audio/dungeon.wav").getAbsolutePath());
    	Link link = new Link(128, 88);
        Profilo profilo = new Profilo("res/profili.csv");

        GameView view = new GameView(link, profilo);

        InputHandler inputHandler = new InputHandler();
        view.getGamePanel().addKeyListener(inputHandler);
        view.getGamePanel().setFocusable(true);

        new GameController(link, view, profilo, inputHandler);
    }
}