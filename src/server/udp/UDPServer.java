package server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer implements Runnable {

    private final int port;

    public UDPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("[UDP 서버 시작] 포트 " + port);

            while (true) {
                byte[] buffer = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // 받은 패킷을 UDPManager에 위임
                UDPManager.manage(packet, socket);
            }
        } catch (Exception e) {
            System.out.println("[UDP 오류] " + e.getMessage());
        }
    }
}
