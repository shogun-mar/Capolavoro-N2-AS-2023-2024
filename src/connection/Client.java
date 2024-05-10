package connection;

import game.ControllerClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client implements Runnable{

    public ControllerClient controller;
    private final DatagramSocket socket;

    public Client(ControllerClient controller, DatagramSocket socket) {
        this.controller = controller;
        this.socket=socket;
    }

    @Override
    public void run() {
        byte[] buf = new byte[300];
        while(true){
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                int bufLength= buf.length;
                for (int i = 0; i < buf.length-1; i++) {
                    if(buf[i]==(byte)99 && buf[i+1]==(byte)99){
                        bufLength=i;
                        break;
                    }
                }
                if(buf[0]==(byte)2){
                    controller.controller.start();
                }
                controller.reloadGame(buf, bufLength);
                controller.reload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
