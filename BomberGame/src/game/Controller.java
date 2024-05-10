package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import component.*;
import connection.*;

public class Controller implements KeyListener {

    private final Game game;
    private final int id;
    private Visualizer visualizer;
    private final int[] k;
    private Server server;

    public Controller(Game game, int id, int[] k) {
        this.game = game;
        this.id = id;
        visualizer=null;
        this.k = k;
        this.server=null;
    }

    @Override
    public void keyTyped(KeyEvent event) {
        if(event.getKeyChar()==k[0]){
            game.goUP(id);
        }
        if(event.getKeyChar()==k[1]){
            game.goDW(id);
        }
        if(event.getKeyChar()==k[2]){
            game.goSX(id);
        }
        if(event.getKeyChar()==k[3]){
            game.goDX(id);
        }
        if(event.getKeyChar()==k[4]){
            TriPair<Integer, Integer, Integer> bomb = game.dropBomb(id);
            if(bomb!=null){
                Thread thread = new Thread(new Bomb(bomb, game, this));
                thread.start();
            }
        }
        sendData();
        reload();
    }

    public void reload(){
        visualizer.reload();
    }

    public void sendData(){
        if(server!=null) {
            try {
                server.sendData();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void start(){
        game.start();
        if(visualizer!=null) {
            visualizer.startTimer();
        }
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setV(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    //Metodi vuoti per rendere concreta la classe

    @Override
    public void keyPressed(KeyEvent event) {}

    @Override
    public void keyReleased(KeyEvent event) {}
}
