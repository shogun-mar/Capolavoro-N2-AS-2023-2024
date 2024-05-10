package graphic;

import game.ControllerClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class clientStartingWindow extends JFrame implements ActionListener {

    //Controller per avviare il gioco come client
    private final ControllerClient controllerClient;

    //Decoration Panel
    private final JPanel decorationPanel = new JPanel(); //Pannello per la gif di decorazione
    private final JLabel decorationLabel = new JLabel(new ImageIcon("graphics" + File.separator + "decoration" + File.separator + "waitingBomb.gif"));

    //Ready Panel
    private final JPanel readyPanel = new JPanel(); //Pannello per il bottone ready
    private final JButton readyButton = new JButton("Ready");

    //Font
    private final Font pixelFont;

    public clientStartingWindow(ControllerClient controllerClient){
        super("Connecting to the server...");
        this.controllerClient = controllerClient;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS)); //Layout che organizza i componenti secondo l'asse delle y
        this.setLocationRelativeTo(null); //Posiziona la finestra al centro dello schermo

        //Carica il font
        pixelFont = new pixelFont().getPixelFont();

        //DecorationPanel
        decorationPanel.setPreferredSize(new Dimension(800, 400));
        decorationPanel.setBackground(Color.WHITE);
        decorationPanel.add(decorationLabel);
        this.add(decorationPanel);

        //ReadyPanel
        readyPanel.setPreferredSize(new Dimension(800, 200)); //setPreferredSize va in modo che le dimensioni del componente non possano essere modificate da un layout manager
        readyPanel.setBackground(Color.WHITE);
        readyButton.setFont(pixelFont);
        readyButton.addActionListener(this);
        readyButton.setContentAreaFilled(false); //Rimuove il background del bottone
        readyButton.setBorderPainted(false); //Rimuove il bordo del bottone
        readyPanel.add(readyButton);
        this.add(readyPanel);

        //Modifica icona della finestra
        this.setIconImage(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"icon.png").getImage());

        this.setVisible(true); //Rende visibile la finestra (alla fine perchè altrimenti non si vedono le modifiche fatte ai pannelli)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == readyButton){
            controllerClient.startGame();
            this.dispose();
        }
    }
}
