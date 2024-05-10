package graphic;

import game.Controller;
import game.Game;
import game.Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class offlineActionListener implements ActionListener {

    private final Component[] buttonsToReveal;
    private final Component[] buttonsToHide;
    private final JFrame frame;

    private Game game;
    private Visualizer visualizer;

    public offlineActionListener(Component[] buttonsToReveal, Component[] buttonsToHide, JFrame frame) {
        this.buttonsToReveal = buttonsToReveal;
        this.buttonsToHide = buttonsToHide;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        JFrame vis; //Variabile per poter passare visualizer a keybindWindow
        switch (actionCommand) {
            case "Offline": // Se viene premuto il JButton "Offline"
                for (Component buttonToReveal : buttonsToReveal) {
                    JButton button = (JButton) buttonToReveal;
                    if (!button.getText().contains("Offline")) { // Mostro e abilito i pulsanti solamente se non contengono la parola "Offline"
                        button.setVisible(true);
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false); //Disabilito il pulsante "Offline"
                    }
                }
                for (Component buttonToHide : buttonsToHide) {
                    JButton button = (JButton) buttonToHide;
                    if (!button.getText().contains("Online")) { // Nascondo e disattivo i pulsanti solamente se non contengono la parola "Online"
                        buttonToHide.setVisible(false);         // ovvero i pulsanti con i numeri e "Join Game"
                        buttonToHide.setEnabled(false);
                    } else {
                        buttonToHide.setEnabled(true); // Riattiva il pulsante "Online"
                    }
                }
                break;
            case "2 Players": // Avvia gioco offline con due giocatori
                frame.dispose();
                vis = start(2);
                new keybindWindow(2, vis);
                break;
            case "3 Players": // Avvia gioco offline con tre giocatori
                frame.dispose();
                vis = start(3);
                new keybindWindow(3, vis);
                break;
            case "4 Players": // Avvia gioco offline con quattro giocatori
                frame.dispose();
                vis = start(4);
                new keybindWindow(4, vis);
                break;
        }
    }

    private JFrame start(int players){ //Mi faccio ritornare visualizer per poterlo passare a keybindWindow
        int [][] key = new int[][]{{'w', 's', 'a', 'd', 'b'}, {'i', 'k', 'j', 'l', '-'}, {'t', 'g', 'f', 'h', 'y'}, {'8', '5', '4', '6', '0'}};//sistemare tasti

        Controller[] controller;
        controller=new Controller[players];

        game = new Game();
        visualizer = new Visualizer(game);

        for (int i = 0; i < players; i++) {
            controller[i] = new Controller(game, game.addPlayer(), key[i]);
            visualizer.addController(controller[i]);
            controller[i].setV(visualizer);
        }

        game.start();
        visualizer.startTimer();
        visualizer.reload();
        return visualizer;
    }
}