package graphic;
import connection.Server;
import game.Controller;
import game.Game;
import game.Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class serverStartingWindow extends JFrame implements ActionListener {

    //Riferimento al visualizer per poterlo poi chiudere nel caso in cui si chiuda questa finestra
    Visualizer v;

    //Variabili di gioco
    private Server srv;
    private boolean start;

    //Font
    private final Font pixelFont;

    //Pannelli
    private final JPanel decorationPanel = new JPanel(); //Pannello per la gif di decorazione
    private final JPanel waitingPanel = new JPanel(); //Pannello per il numero di giocatori pronti
    private final JPanel confermationPanel = new JPanel(); //Pannello per il bottone di conferma

    //Decoration Panel
    private final ImageIcon decorationIcon = new ImageIcon("graphics" + File.separator + "decoration" + File.separator + "waitingBomb.gif"); //400x400
    private final JLabel decorationLabel = new JLabel(decorationIcon);

    private final JLabel ipLabel = new JLabel("IP Address: ");

    //waitingPanel
    private final JLabel waitingLabel = new JLabel("?/? players ready"); //Inizializzato così per evitare errori
    //Esempio: 3/4 players ready

    //ButtonPanel
    private final JButton startButton = new JButton("Start Game"); //Bottone per iniziare la partita, attivo solamente quando tutti i giocatori sono pronti

    public serverStartingWindow() {
        super("Waiting for the other players...");
        srv=null;
        try{
            Game game = new Game();

            Controller controller  = new Controller(game,  game.addPlayer(), new int[]{'w', 's', 'a', 'd', 'b'});

            srv = new Server(game, controller);
            Thread thread = new Thread(srv);
            thread.start();

            v = new Visualizer(game);
            v.addController(controller);
            controller.setV(v);

            controller.setServer(srv);
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }
        start=false;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setLocationRelativeTo(null); //Posiziona la finestra al centro dello schermo
        
        //Modifica icona della finestra
        this.setIconImage(new ImageIcon("graphics"+File.separator+"decoration"+File.separator+"icon.png").getImage());

        //Carica il font
        pixelFont = new pixelFont().getPixelFont();

        //DecorationPanel
        decorationPanel.add(decorationLabel);
        decorationPanel.setPreferredSize(new Dimension(800, 400));
        decorationPanel.setBackground(Color.WHITE);
        this.add(decorationPanel);


        ipLabel.setFont(pixelFont);

        String ip="";
        try{
          ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        ipLabel.setText("IP Address: "+ip);

        //WaitingPanel
        waitingPanel.setLayout(new GridLayout(0 ,1));
        waitingLabel.setFont(pixelFont);
        //waitingPanel.add(ipLabel);
        waitingPanel.add(waitingLabel);
        waitingPanel.setPreferredSize(new Dimension(800, 100));
        waitingPanel.setBackground(Color.WHITE);
        this.add(waitingPanel);

        
        //ConfermationPanel
        confermationPanel.setLayout(new FlowLayout());
        startButton.setEnabled(false); //Disabilitato finchè il numero di giocatori non è uguale al numero di conferme
        startButton.setFont(pixelFont);
        startButton.setContentAreaFilled(false); //Rimuove il background del bottone
        startButton.setBorderPainted(false); //Rimuove il bordo del bottone
        confermationPanel.add(startButton);
        confermationPanel.setPreferredSize(new Dimension(800, 100));
        confermationPanel.setBackground(Color.WHITE);
        this.add(confermationPanel);

        this.setVisible(true); //Rende visibile la finestra (alla fine perchè altrimenti non si vedono le modifiche fatte ai pannelli)

        startButton.addActionListener(this);
        Thread thread = new Thread(this::update);
        thread.start();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                v.dispose(); // Chiude la finestra visualizer
                new selectionWindow();
            }
        });
    }

    public void update(){
        int ready, totalPlayers;
        while(!start){
            ready=srv.getReady();
            totalPlayers=srv.getPlayer();
            waitingLabel.setText(ready+"/"+totalPlayers+" players ready");
            startButton.setEnabled(ready == totalPlayers);
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
            if(srv!=null) {
                try {
                    if(srv.start()) {
                        start=true;
                        this.dispose();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}