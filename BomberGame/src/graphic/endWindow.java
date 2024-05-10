package graphic;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class endWindow extends JFrame {

    JPanel messagePanel = new JPanel();
    JLabel imageLabel = new JLabel(new ImageIcon("graphics" + File.separator + "decoration" + File.separator + "gameOver.png"));
    JPanel interactionPanel = new JPanel();
    JButton restartButton = new JButton("Restart");
    JButton exitButton = new JButton("Exit");
    JPanel winnerPanel = new JPanel();
    JLabel winnerLabel = new JLabel();

    public endWindow(String winner){
        super("Game Over");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLayout(new GridLayout(3,1));
        // Posiziona la finestra al centro dello schermo
        this.setLocationRelativeTo(null);

        //Font
        Font pixelFont = new pixelFont().getPixelFont().deriveFont(64f);

        //Action Listener
        endActionListener actionListener = new endActionListener(this);

        //Message Panel
        messagePanel.setLayout(new FlowLayout());
        messagePanel.add(imageLabel);
        this.add(messagePanel);

        //Winner Panel
        winnerPanel.setLayout(new FlowLayout());
        winnerPanel.add(winnerLabel);
        if (!winner.equals("Draw!")) { //Se non è uguale a Draw!, quindi se ha vinto un player setta Player idPLayer wins!
            winnerLabel.setText(winner + " wins!");
        } else { //Se invece è uguale a Draw! setto quello
            winnerLabel.setText(winner);
        }
        winnerLabel.setFont(pixelFont);
        this.add(winnerPanel);

        //Interaction Panel
        interactionPanel.setLayout(new GridLayout(1,2));
        //interactionPanel.add(restartButton);
        interactionPanel.add(exitButton);
        this.add(interactionPanel);

        //Restart button
        restartButton.setContentAreaFilled(false); //Rimuove il background del bottone
        restartButton.setBorderPainted(false); //Rimuove il bordo del bottone
        restartButton.setFont(pixelFont);
        restartButton.addActionListener(actionListener);

        //Exit button
        exitButton.setContentAreaFilled(false); //Rimuove il background del bottone
        exitButton.setBorderPainted(false); //Rimuove il bordo del bottone
        exitButton.setFont(pixelFont);
        exitButton.addActionListener(actionListener);

        //Modifica icona della finestra
        this.setIconImage(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"icon.png").getImage());

        this.setVisible(true); //Rende visibile la finestra (alla fine perchè altrimenti non si vedono le modifiche fatte ai pannelli)
    }
}
