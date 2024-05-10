package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.*;

public class ControllerClient implements KeyListener {
    private final Game game;
    private final InetAddress ip;
    private final int port;
    private final DatagramSocket socket;
    private Visualizer visualizer;
    public Controller controller;

    public ControllerClient(Game game, String ip, int port, int internalPort) throws UnknownHostException, SocketException {
        this.ip = InetAddress.getByName(ip);
        this.port = port;
        socket = new DatagramSocket(internalPort);
        visualizer =null;
        this.game = game;
        controller =new Controller(game, -1, new int[]{119, 115, 100, 97, 98});
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                move(0);
                break;
            case 's':
                move(1);
                break;
            case 'd':
                move(2);
                break;
            case 'a':
                move(3);
                break;
            case 'b':
                bomb();
                break;
            case 'x':
                startGame();
                break;
        }
        reload();
    }

    public void reload(){
        visualizer.reload();
    }

    public void reloadGame(byte[] buf, int len){
        game.reloadMatrix(buf, len);
    }

    public void send(byte[] buf){
        try {
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(int direction) {
        send(new byte[]{(byte) 3, (byte) direction});
    }

    public void bomb() {
        send(new byte[]{(byte) 4});
    }

    public void addPlayer(){
        send(new byte[]{(byte) 1});
    }

    public void startGame(){
        send(new byte[]{(byte) 2});
    }

    public void setVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
        controller.setV(visualizer);
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    //Metodi vuoti per rendere concreta la classe

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
