package server;

import server.tcp.TCPServer;
import server.udp.UDPServer;

public class ServerMain {

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(Config.TCP_PORT);
        UDPServer udpServer = new UDPServer(Config.UDP_PORT);

        Thread tcpThread = new Thread(tcpServer, "TCP-Server");
        Thread udpThread = new Thread(udpServer, "UDP-Server");

        tcpThread.start();
        udpThread.start();

        System.out.println("[서버 시작] TCP:" + Config.TCP_PORT + " / UDP:" + Config.UDP_PORT);
    }
}
