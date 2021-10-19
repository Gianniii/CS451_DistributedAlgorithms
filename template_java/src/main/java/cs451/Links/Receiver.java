package cs451.Links;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

/** code based on https://www.baeldung.com/udp-in-java **/

public class Receiver extends Thread {

    private final DatagramSocket socket;
    private final StubbornLinkWithAck stubbornLinkWithAck;
    
    public Receiver(int hostPort, StubbornLinkWithAck stubbornLinkWithAck) throws SocketException {
        socket = new DatagramSocket(hostPort);
        this.stubbornLinkWithAck = stubbornLinkWithAck;
    }

    @Override
    public void run(){
        while (true){
            try{
                byte[] buf = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                stubbornLinkWithAck.deliver(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}