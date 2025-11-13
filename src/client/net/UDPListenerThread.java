package client.net;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import client.ui.ClientDispatcher;
import protocol.udp.Stroke;

//역할: 그림좌표 수신

public class UDPListenerThread implements Runnable {
    private final DatagramSocket udp;
    private final ClientDispatcher dispatcher;

    public UDPListenerThread(DatagramSocket udp, ClientDispatcher dispatcher) {
        this.udp = udp;
        this.dispatcher = dispatcher;
    }

    public void run() {
        try {
            byte[] buf = new byte[4096];
            DatagramPacket pkt = new DatagramPacket(buf, buf.length);
            while (true) {
                udp.receive(pkt);
                ByteArrayInputStream bais = new ByteArrayInputStream(pkt.getData(), 0, pkt.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object obj = ois.readObject();
                if (obj instanceof Stroke) {
                    dispatcher.onUDP((Stroke) obj);
                }
            }
        } catch (Exception e) {
            dispatcher.onUDPClosed(e);
        }
    }
}
