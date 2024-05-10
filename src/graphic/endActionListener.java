package graphic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class endActionListener implements ActionListener{

    JFrame frame;

    public endActionListener(JFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        if (actionCommand.equals("Restart")) {
            //Riavviare il gioco (riapriendo la finestra di selezione)
            new selectionWindow();
            frame.dispose(); //Pulizia del JFrame dalla RAM
        } else if (actionCommand.equals("Exit")) {
            //Termina il processo
            System.exit(0);
        }
    }
}
