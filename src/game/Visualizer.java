package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.time.Instant;
import component.*;
import graphic.endWindow;
import graphic.pixelFont;

public class Visualizer extends JFrame{

    private final Game game;
    private boolean finish = false;
    private final Cell[][] last;
    private final JLabel[][] campo;
    private final int CELL_DIM = 80; //Costante che determina la dimensione delle JLabel usate per il campo di gioco
    private final JLabel timerText; // JLabel usato per visualizzare il tempo di gioco

    //stringhe per gli indirizzi delle immagini (le immagini sono da aggiornare con lo sfondo verde però)
    final String common_address ="graphics"+File.separator;

    //Variazioni dell'immagine della bomba
    String bomb_add = common_address + "bombs" + File.separator + "bomb_%d.png";//1, 2, 3

    //Immagini ostacoli
    final String obstacles_add = common_address + "obstacles" + File.separator + "%s.png";//bush, crate

    //Immagini background zone libere
    final String background_add = common_address + "background" + File.separator +"background.png"; //Background con erba

    //Immagini background zone occupate
    final String Wall_add = common_address + "walls/interiorWall.png";

    //Variazioni dell'immagine di player (non abbiamo fatto in tempo ad implementarlo)

    //STILL
    //id: 1, 2, 3, 4
    //side: back, front, left, right
    //String playerStill = common_address + "player%d" + File.separator + "%sStill.png";

    //MOVEMENT
    //id: 1, 2, 3, 4
    //side: back, front, left, right
    //type: 1, 2, 3
    String playerMovement = common_address + "player%d" + File.separator + "%sMoving%d.png";
    ImageIcon[] playerIcon = new ImageIcon[4];
    ImageIcon wall = new ImageIcon(Wall_add);
    ImageIcon[] obs = {new ImageIcon(String.format(String.format(obstacles_add, "bush"))), new ImageIcon(String.format(String.format(obstacles_add, "crate")))};
    ImageIcon[] bomb = new ImageIcon[3];
    ImageIcon back = new ImageIcon(background_add);
    ImageIcon[] explosion = new ImageIcon[7];

    public Visualizer(Game game){
        super("Bomberman");

        //Carica il font
        Font pixelFont = new pixelFont().getPixelFont().deriveFont(24f);

        for (int i = 0; i < playerIcon.length; i++) {
            playerIcon[i] = new ImageIcon(String.format(playerMovement, i+1, "front", 1));
        }

        for (int i = 0; i < bomb.length; i++) {
            bomb[i] = new ImageIcon(String.format(bomb_add, i+1));
        }

        for (int i = 0; i < explosion.length; i++) {
            explosion[i] = new ImageIcon(String.format(common_address + "blast" + File.separator + "tile%d.png", i));
        }

        this.game = game;
        last=new Cell[11][15];
        campo = new JLabel[game.getBoard().length][game.getBoard()[0].length];

        //Pannello contenente la interfaccia dell'utente
        JPanel panelUI = new JPanel();
        panelUI.setPreferredSize(new Dimension(campo[0].length*CELL_DIM, 50));
        panelUI.setLayout(new FlowLayout(FlowLayout.CENTER));
        timerText = new JLabel(); //JPanel usato per visualizzare il tempo di gioco
        timerText.setBounds(50,50,50,50);
        timerText.setText("00:00"); //Inizializza il tempo di gioco a 00:00
        timerText.setFont(pixelFont);
        panelUI.add(timerText);

        init();
        reload();

        //JPanel contenente il campo di gioco
        JPanel panelGioco = new JPanel();
        panelGioco.setPreferredSize(new Dimension(campo[0].length*CELL_DIM, campo.length*CELL_DIM));
        //Aggiunge grid layout al JPanel contenente il campo di gioco
        panelGioco.setLayout(new GridLayout(game.getBoard().length, game.getBoard()[0].length));
        //Aggiunta elementi di campo al panelGioco
        for (JLabel[] jLabels : campo) {
            for (int j = 0; j < campo[0].length; j++) {
                panelGioco.add(jLabels[j]);
            }
        }

        //Comportamento e caratteristiche del frame
        this.setLayout(new FlowLayout());
        this.setSize(campo[0].length*CELL_DIM, campo.length*CELL_DIM + 98); //
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.add(panelUI);
        this.add(panelGioco);
        // Posiziona la finestra al centro dello schermo
        this.setLocationRelativeTo(null);
    }

    private void init(){ //Inizializza le JLabel usate per il campo di gioco
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                campo[i][j] = new JLabel();
                campo[i][j].setSize(CELL_DIM, CELL_DIM);
                campo[i][j].setVisible(true);
            }
        }
    }

    public void reload(){ //Aggiorna lo stato del campo a livello grafico
        if(game.isOver()){
            if(!finish){
                new endWindow(game.getWin());
                this.dispose();
                finish = true;
            }
        }

        Cell[][] board = game.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                if(last[i][j] != null && last[i][j].equals(board[i][j]) && !board[i][j].bomb){
                    continue;
                }
                if (board[i][j].player)
                    //campo[i][j].setText("P");
                    campo[i][j].setIcon(playerIcon[board[i][j].id-1]);
                else if (board[i][j].wall)
                    //campo[i][j].setText("W");
                    campo[i][j].setIcon(wall);
                else if (board[i][j].obs_bush)
                    //campo[i][j].setText("B");
                    campo[i][j].setIcon(obs[0]);
                else if (board[i][j].obs_crate)
                    //campo[i][j].setText("C");
                    campo[i][j].setIcon(obs[1]);
                else if(!board[i][j].bomb)
                    campo[i][j].setIcon(back);
                else
                    for (int k = 0; k < game.getBombs().size(); k++) {
                        if(game.getBombs().get(k).s == i && game.getBombs().get(k).f == j){
                            if(game.getBombs().get(k).t ==0){
                                int x = j;
                                int y = i;
                                new Thread(()->loadBomb(x, y)).start();
                                game.getBombs().get(k).t=-1;
                            }else if(game.getBombs().get(k).t>0 && game.getBombs().get(k).t<=3)
                                campo[i][j].setIcon(bomb[game.getBombs().get(k).t -1]);
                        }
                    }
                //3 --> bomba appena piazzata, 2--> bomba accesa, 1 --> bomba che sta per esplodere


                last[i][j] = new Cell(board[i][j]);
            }
        }
    }

    public void loadImg(int x, int y, int index) {
        campo[y][x].setIcon(explosion[index]);
    }

    public void loadBomb(int x, int y){

        loadImg(x, y, 0);

        Cell[][] board = game.getBoard();

        Pair<Integer, Integer>[] poss = new Pair[]{new Pair<>(x, y+1), new Pair<>(x, y-1), new Pair<>(x+1, y), new Pair<>(x-1, y)};
        Pair<Integer, Integer>[] poss1 = new Pair[]{new Pair<>(x, y+2), new Pair<>(x, y-2), new Pair<>(x+2, y), new Pair<>(x-2, y)};

        for (int i = 0; i < 4; i++) {
            try{
                if(board[poss[i].s][poss[i].f].wall){
                    continue; //Questo statement salta alla prossima iterazione del codice
                }
            }catch (Exception e){
                //e.printStackTrace();
                continue; //Questo statement salta alla prossima iterazione del codice
            }
            campo[poss[i].s][poss[i].f].setIcon(explosion[i+1]);
            try{
                if(board[poss1[i].s][poss1[i].f].wall){
                    continue; //Questo statement salta alla prossima iterazione del codice
                }
            }catch (Exception e){
                //e.printStackTrace();
                continue; //Questo statement salta alla prossima iterazione del codice
            }
            campo[poss[i].s][poss[i].f].setIcon(explosion[i<2?5:6]);
            campo[poss1[i].s][poss1[i].f].setIcon(explosion[i+1]);
        }

        try{
            Thread.sleep(750);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        for (int i = 0; i < 4; i++) {
            try{
                last[poss1[i].s][poss1[i].f]=null;
            }catch (Exception e){
                //e.printStackTrace();
            }
            try{
                last[poss[i].s][poss[i].f]=null;
            }catch (Exception e){
                //e.printStackTrace();
            }
        }
        last[y][x]=null;
        reload();
    }

    public void addController(KeyListener c){
        addKeyListener(c);
    }

    public void startTimer(){
        Thread thread = new Thread(() -> {
            long start, now, diff, min, sec;
            start = Instant.now().getEpochSecond();
            while(!game.isOver()){
                now = Instant.now().getEpochSecond();
                diff = now - start;
                min = diff / 60;
                sec = diff % 60;
                timerText.setText(String.format("%02d:%02d", min, sec));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}