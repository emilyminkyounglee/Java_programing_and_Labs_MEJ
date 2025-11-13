package server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// UDP는 Stroke 객체를 그대로 브로드캐스트하는 간단한 구조로 구현
public class UDPManager {

    // UDP로 그림을 보낸 적 있는 클라이언트들의 주소 목록
    private static final Set<InetSocketAddress> clients =
            ConcurrentHashMap.newKeySet();

    public static void manage(DatagramPacket packet, DatagramSocket socket) {
        try {
            InetSocketAddress sender =
                    new InetSocketAddress(packet.getAddress(), packet.getPort());
            clients.add(sender);

            // 받은 Stroke 직렬화 바이트를 그대로 다른 클라이언트들에게 전파
            for (InetSocketAddress target : clients) {
                // 필요하면 자기 자신 제외도 가능
                // if (target.equals(sender)) continue;

                DatagramPacket out = new DatagramPacket(
                        packet.getData(),
                        packet.getLength(),
                        target.getAddress(),
                        target.getPort()
                );
                socket.send(out);
            }
        } catch (Exception e) {
            System.err.println("[UDP 브로드캐스트 오류] " + e.getMessage());
        }
    }
}
