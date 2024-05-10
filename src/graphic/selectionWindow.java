package graphic;

import javax.swing.*;
import java.awt.*;
import java.io.File;

//Frame di inizio gioco usato per la selezione del numero di giocatori e la modalità di gioco
public class selectionWindow extends JFrame {

    //Panelli
    private final JPanel logoPanel = new JPanel();
    private final JPanel selectionPanel = new JPanel();
    private final JPanel offlinePanel = new JPanel();
    private final JPanel onlinePanel = new JPanel();
    private final JPanel foregroundPanel = new JPanel();
    private final JPanel backgroundPanel = new JPanel();
    //Componenti offline
    private final JButton offlineButton = new JButton("Offline");
    private final JButton offlineButton_twoPlayer = new JButton("2 Players");
    private final JButton offlineButton_threePlayer = new JButton("3 Players");
    private final JButton offlineButton_fourPlayer = new JButton("4 Players");
    private final Component[] offlineComponents;
    //Componenti online
    private final JButton onlineButton = new JButton("Online");
    private final JButton onlineButton_twoPlayer = new JButton("2 Players");
    private final JButton onlineButton_threePlayer = new JButton("3 Players");
    private final JButton onlineButton_fourPlayer = new JButton("4 Players");
    private final JButton serverbutton = new JButton("Server");
    private final JButton joinButton = new JButton("Join Game");
    private final Component[] onlineComponents;;
    //Immagine decoration
    JLabel logoLabel = new JLabel(new ImageIcon( "graphics"+File.separator+"decoration"+File.separator+"BombermanLogo.png"));

    //Font
    Font pixelFont;

    public selectionWindow(){  // ForegroundPanel contiene logoPanel e selectionPanel ed ha un layout a griglia 2x1
        //Caratteristiche e comportamento della frame
        super("BomberMan Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 600);
        // Posiziona la finestra al centro dello schermo
        this.setLocationRelativeTo(null);

        // Crea un nuovo JPanel che fungerà da contenitore per foregroundPanel e backgroundPanel
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));

        //Foreground Panel
        foregroundPanel.setOpaque(false);
        foregroundPanel.setLayout(new GridLayout(2,1));
        foregroundPanel.add(logoPanel);
        foregroundPanel.add(selectionPanel);
        overlayPanel.add(foregroundPanel);

        //Background Panel
        JLabel backgroundLabel = new JLabel(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"background"+generateRandomInt()+".png"));
        backgroundPanel.add(backgroundLabel);
        overlayPanel.add(backgroundPanel);

        //JPanel per la decorazione
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel);

        //JPanel per la selezione
        selectionPanel.setOpaque(false);
        selectionPanel.setLayout(new GridLayout(1,2));
        selectionPanel.add(offlinePanel);
        selectionPanel.add(onlinePanel);

        //JPanel per la selezione offline
        offlinePanel.setOpaque(false);
        offlinePanel.setLayout(new BoxLayout(offlinePanel, BoxLayout.Y_AXIS));
        offlinePanel.add(offlineButton);
        offlinePanel.add(offlineButton_twoPlayer);
        offlinePanel.add(offlineButton_threePlayer);
        offlinePanel.add(offlineButton_fourPlayer);
        //Centrare gli elementi nel pannello (perchè il box layout altrimenti li allinea a sinistra)
        offlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        offlineButton_twoPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        offlineButton_threePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        offlineButton_fourPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        offlineComponents = offlinePanel.getComponents(); //Lista dei componenti del panello offline (viene poi utilizzata per i listener)

        //JPanel per la selezione online
        onlinePanel.setOpaque(false);
        onlinePanel.setLayout(new BoxLayout(onlinePanel, BoxLayout.Y_AXIS));
        onlinePanel.add(onlineButton);
        onlinePanel.add(serverbutton);
        onlinePanel.add(joinButton);
        //Centrare gli elementi nel pannello (perchè il box layout altrimenti li allinea a sinistra)
        onlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        serverbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        onlineComponents = onlinePanel.getComponents(); //Lista dei componenti del panello online (viene poi utilizzata per i listener)

        //Carica il font
        pixelFont = new pixelFont().getPixelFont();

        //Gestione componenti offline
        offlineActionListener offlineAL = new offlineActionListener(offlineComponents,onlineComponents, this); ;
        for (Component component : offlineComponents){
            JButton button = (JButton) component;
            if (button != offlineButton) { //Escludo il bottone offline perchè deve rimanere sempre visibile e attivo
                button.setVisible(false);
                button.setEnabled(false);
            }
            button.setFont(pixelFont);
            button.setContentAreaFilled(false); //Rimuove il background del bottone
            button.setBorderPainted(false); //Rimuove il bordo del bottone
            button.addActionListener(offlineAL);
        }

        //Gestione componenti online
        onlineActionListener onlineAL = new onlineActionListener(onlineComponents,offlineComponents, this); ;
        for (Component component : onlineComponents){
            JButton button = (JButton) component;
            if (button != onlineButton) { //Escludo il bottone online perchè deve rimanere sempre visibile e attivo
                button.setVisible(false);
                button.setEnabled(false);
            }
            button.setFont(pixelFont);
            button.setContentAreaFilled(false); //Rimuove il background del bottone
            button.setBorderPainted(false); //Rimuove il bordo del bottone
            button.addActionListener(onlineAL);
        }
        //Aggiungo overlayPanel alla finestra
        this.add(overlayPanel);
        //Modifica icona della finestra
        this.setIconImage(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"icon.png").getImage());
        this.setVisible(true); //Rende visibile la finestra (alla fine perchè altrimenti non si vedono le modifiche fatte ai pannelli)
    }
    public int generateRandomInt() { //Utilizzato per randomizzare il background
        return (int) (Math.random() * 3) + 1;
    }
}


