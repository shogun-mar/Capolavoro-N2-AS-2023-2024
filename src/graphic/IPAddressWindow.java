package graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class IPAddressWindow extends JFrame {

    //Carica il font
    Font pixelFont = new pixelFont().getPixelFont().deriveFont(24f);

    JTextField ipField1 = new JTextField(3); //Primo otteto dell'indirizzo IP
    JTextField ipField2 = new JTextField(3); //Secondo otteto dell'indirizzo IP
    JTextField ipField3 = new JTextField(3); //Terzo otteto dell'indirizzo IP
    JTextField ipField4 = new JTextField(3); //Quarto otteto dell'indirizzo IP
    JLabel dot1 = new JLabel("."); //Punto tra il primo e il secondo otteto
    JLabel dot2 = new JLabel("."); //Punto tra il secondo e il terzo otteto
    JLabel dot3 = new JLabel("."); //Punto tra il terzo e il quarto otteto

    JButton confirmButton = new JButton("Conferma");
    JLabel dialogueLabel = new JLabel("Inserire l'indirizzo IP del server:");
    JPanel ipPanel = new JPanel(); //Contiene i JTextField e i JLabel per l'inserimento dell'indirizzo IP
    JPanel buttonPanel = new JPanel(); //Contiene il bottone di conferma
    JPanel dialoguePanel = new JPanel(); //Contiene la label di dialogo
    JPanel backgroundPanel = new JPanel(); //Contiene la JLabel per lo sfondo
    JPanel foregroundPanel = new JPanel(); //Contiene ipPanel, buttonPanel e dialoguePanel
    JPanel overlayPanel = new JPanel(); // Crea un nuovo JPanel che fungerà da contenitore per foregroundPanel e backgroundPanel

    //ActionListener
    IPListener ipListener = new IPListener(new JTextField[]{ipField1, ipField2, ipField3, ipField4}, dialogueLabel, this);

    public IPAddressWindow(){
        super("Inserire indirizzo IP");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Chiude solo la finestra, non il programma
        this.setResizable(false);
        this.setSize(600, 200);
        // Posiziona la finestra al centro dello schermo
        this.setLocationRelativeTo(null);

        //Overlay layout consente di sovrapporre panel con eventuale trasparenza
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));

        //Foreground Panel
        foregroundPanel.setLayout(new GridLayout(3,1));
        foregroundPanel.setOpaque(false); //Necessario perchè si veda lo sfondo
        foregroundPanel.add(dialoguePanel);
        foregroundPanel.add(ipPanel);
        foregroundPanel.add(buttonPanel);
        overlayPanel.add(foregroundPanel);

        //Background Panel
        backgroundPanel.add(new JLabel(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"backgroundIP.png"))); //Aggiunge un'immagine di sfondo al pannello backgroundPanel
        overlayPanel.add(backgroundPanel);

        //Dialogue Panel
        dialoguePanel.setOpaque(false);
        dialoguePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialogueLabel.setFont(pixelFont);
        dialoguePanel.add(dialogueLabel);

        //IP Panel
        ipPanel.setOpaque(false);
        ipPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        ipPanel.add(ipField1);
        ipPanel.add(dot1);
        ipPanel.add(ipField2);
        ipPanel.add(dot2);
        ipPanel.add(ipField3);
        ipPanel.add(dot3);
        ipPanel.add(ipField4);

        //Button Panel
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(confirmButton);
        confirmButton.setContentAreaFilled(false); //Rimuove il background del bottone
        confirmButton.setBorderPainted(false); //Rimuove il bordo del bottone
        confirmButton.setFont(pixelFont);
        confirmButton.addActionListener(ipListener);

        //Aggiungo overlayPanel alla finestra
        this.add(overlayPanel);

        //Modifica icona della finestra
        this.setIconImage(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"icon.png").getImage());
        this.setVisible(true); //Rende visibile la finestra (alla fine perchè altrimenti non si vedono le modifiche fatte ai pannelli)

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new selectionWindow();
            }
        });
    }
}
