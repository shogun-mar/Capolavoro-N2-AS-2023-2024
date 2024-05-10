package connection;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import game.*;
import component.*;

public class Server implements Runnable{

    private final DatagramSocket socket;
    private final ArrayList<Pair<String, Integer>> players;
    private final Game game;
    private Controller controller;
    private int ready = 1;

    public Server(Game g, Controller c) throws SocketException {
        socket = new DatagramSocket(50000);
        players = new ArrayList<Pair<String, Integer>>(4);
        this.game = g;
        this.controller =c;
    }

    @Override
    public void run() {
        // header primo byte
        // 1 aggiungi giocatore
        // 2 start Game
        // 3 muovi giocatore
        // 4 droppa bomba
        while(true){
            try {

                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String cl = packet.getAddress().getHostAddress();

                //aggiungi giocatore
                if(buf[0] == 1){
                    players.add(new Pair<String, Integer>(cl, game.addPlayer()));
                }

                //avvia game
                if(buf[0] == 2){
                    ready++;
                }

                int id=foundID(cl);
                if(id==-1) continue;

                //muovi giocatore
                if(buf[0] == 3){
                    move(id, buf[1]);
                }

                //drop bomb
                if(buf[0] == 4){
                    TriPair<Integer, Integer, Integer> bomb = game.dropBomb(id);
                    if(bomb!=null){
                        Thread t = new Thread(new Bomb(bomb, game, controller));
                        t.start();
                    }
                }
                sendData();
                controller.reload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int foundID(String ip){
        for (Pair<String, Integer> client : players) {
            if (client.f.equals(ip)) return client.s;
        }
        return -1;
    }

    public void sendData() throws IOException {
        byte[] buf = game.createPacket();
        for (Pair<String, Integer> player : players) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(player.f), 50001);
            socket.send(packet);
        }
    }

    public void sendStart() throws IOException {
        byte[] buf = game.createPacket();
        buf[0]=(byte) 2;
        for (Pair<String, Integer> player : players) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(player.f), 50001);
            socket.send(packet);
        }
    }

    //mosse 0-->up, 1-->down, 2-->dx, 3-->sx
    public void move(int id, int direction){
        switch (direction){
            case 0:
                game.goUP(id);
                break;
            case 1:
                game.goDW(id);
                break;
            case 2:
                game.goDX(id);
                break;
            case 3:
                game.goSX(id);
                break;
        }
    }

    public int getReady(){
        return ready;
    }
    public int getPlayer(){
        return players.size()+1;
    }

    public boolean start() throws IOException {
        if(ready == game.getPlayers().size() && ready>1){
            controller.start();
            sendStart();
            return true;
        }
        return false;
    }

}