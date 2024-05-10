package graphic;

import connection.Client;
import game.ControllerClient;
import game.Game;
import game.Visualizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.*;

public class IPListener implements ActionListener {

    private final JFrame frame;
    private final JLabel dialogueLabel;
    private final JTextField[] ipFields;
    Pattern pattern; //Pattern regex per controllare l'indirizzo IP

    public IPListener(JTextField[] ipFields, JLabel dialogueLabel, JFrame frame){
        this.dialogueLabel = dialogueLabel;
        this.ipFields = ipFields;
        this.frame = frame;
    }

    public boolean validateIP() {
        //Pattern regex per controllare l'indirizzo IP
        String ipPattern = "^([01]?[0-9][0-9]?|2[0-4][0-9]|25[0-5])$";
        //Matcher regex per controllare l'indirizzo IP
        Matcher matcher;
        pattern = Pattern.compile(ipPattern);
        for (JTextField octect : ipFields) {
            String octectText = octect.getText();
            if (octectText.isEmpty()) { //Se trova anche un solo campo vuoto ritorna false interrompendo quindi il loop
                return false;
            }
            matcher = pattern.matcher(octectText);
            if (!matcher.matches()) { //Se trova un campo che non rispetta il pattern ritorna false interrompendo quindi il loop
                return false;
            }
        }
        return true; //Se tutti i campi rispettano il pattern ritorna true
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (validateIP()) {
            frame.dispose();

            try{
                String ipServer = ipFields[0].getText() + "." + ipFields[1].getText() + "." + ipFields[2].getText() + "." + ipFields[3].getText();

                Game game = new Game();

                ControllerClient controllerClient = new ControllerClient(game, ipServer, 50000, 50001);//ip-porta server

                Client client = new Client(controllerClient, controllerClient.getSocket());
                Thread thread = new Thread(client);
                thread.start();

                Visualizer visualizer = new Visualizer(game);
                visualizer.addController(controllerClient);

                controllerClient.setVisualizer(visualizer);
                controllerClient.addPlayer();
                new clientStartingWindow(controllerClient);
            } catch (UnknownHostException | SocketException ex) {
                ex.printStackTrace();
            }
        } else {
            dialogueLabel.setText("Inserire un indirizzo IP valido:");
        }
    }
}
