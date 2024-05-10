package graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class onlineActionListener implements ActionListener {

    private final Component[] buttonsToReveal;
    private final Component[] buttonsToHide;
    private final JFrame frame;

    public onlineActionListener(Component[] buttonsToReveal, Component[] buttonsToHide, JFrame frame) {
        this.buttonsToReveal = buttonsToReveal;
        this.buttonsToHide = buttonsToHide;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent event) { //Action Listener per i bottoni "Online" di selectionWindow.java
        String actionCommand = event.getActionCommand();
        switch (actionCommand) {
            case "Online": //Se viene premuto il JButton "Online"
                for (Component buttonToReveal : buttonsToReveal) { //Non serve escludere il JButton "Online" perché setta a true booleane che sono già a true
                    JButton button = (JButton) buttonToReveal;
                    if (!button.getText().contains("Online")) { // Mostro e abilito i pulsanti solamente se non contengono la parola "Online"
                        button.setVisible(true);
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false); //Disabilito il pulsante "Online"
                    }
                }
                for (Component buttonToHide : buttonsToHide) { //Se visibile nasconde i JButton "Online"
                    JButton button = (JButton) buttonToHide;
                    if (button.getText().contains("Player")) { //Nascondo e disattivo i pulsanti solamente se contengono la parola "Player"
                        buttonToHide.setVisible(false);
                        buttonToHide.setEnabled(false);
                    } else {
                        buttonToHide.setEnabled(true); //Riattiva il pulsante "Offline"
                    }
                }
                break;
            case "Server":
                //Avvia gioco online come server
                frame.dispose();
                new serverStartingWindow();
                break;
            case "Join Game":
                //Avvia gioco online come client
                frame.dispose();
                new IPAddressWindow();
                break;
        }
    }
}