package client.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import protocol.MessageEnvelope;
import protocol.udp.Stroke;

public class Network {
    private Socket tcpSocket;
    private ObjectOutputStream tcpOut;
    private ObjectInputStream tcpIn;

    private DatagramSocket udpSocket;
    private InetAddress serverAddr;
    private int udpServerPort;

    public void connectTCP(String host, int port) throws IOException {
        tcpSocket = new Socket(host, port);
        // ObjectOutputStream을 먼저 만들어야 스트림 헤더 충돌이 적음
        tcpOut = new ObjectOutputStream(tcpSocket.getOutputStream());
        tcpIn  = new ObjectInputStream(tcpSocket.getInputStream());
    }

    public void connectUDP(String host, int port) throws Exception {
        udpSocket = new DatagramSocket(); // 임의 포트
        serverAddr = InetAddress.getByName(host);
        udpServerPort = port;
    }

    public void startListeners(Runnable tcpListener, Runnable udpListener) {
        Thread t1 = new Thread(tcpListener);
        t1.setName("TCPListener");
        t1.start();

        Thread t2 = new Thread(udpListener);
        t2.setName("UDPListener");
        t2.start();
    }

    public void sendTCP(MessageEnvelope env) throws IOException {
        synchronized (tcpOut) {
            tcpOut.writeObject(env);
            tcpOut.flush();
        }
    }

    public void sendUDP(Stroke stroke) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(stroke);
        oos.flush();
        byte[] buf = baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, udpServerPort);
        udpSocket.send(packet);
    }

    public ObjectInputStream tcpIn() { return tcpIn; }
    public DatagramSocket udpSocket() { return udpSocket; }

    public void closeQuietly() {
        try { if (tcpIn != null) tcpIn.close(); } catch (Exception e) {}
        try { if (tcpOut != null) tcpOut.close(); } catch (Exception e) {}
        try { if (tcpSocket != null) tcpSocket.close(); } catch (Exception e) {}
        try { if (udpSocket != null) udpSocket.close(); } catch (Exception e) {}
    }
}

