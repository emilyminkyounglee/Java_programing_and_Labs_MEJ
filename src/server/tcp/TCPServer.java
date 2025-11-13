package server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer implements Runnable {

    private final int port;
    private final List<ClientManager> clients = new CopyOnWriteArrayList<>();

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[TCP 서버 시작] 포트 " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[TCP 접속] " + socket.getRemoteSocketAddress());

                try {
                    ClientManager cm = new ClientManager(socket);
                    clients.add(cm);
                    Thread t = new Thread(cm, "Client-" + socket.getPort());
                    t.start();
                } catch (RuntimeException e) {
                    System.err.println("ClientManager 생성 실패: " + e.getMessage());
                    try { socket.close(); } catch (IOException io) {}
                }
            }
        } catch (IOException e) {
            System.err.println("[TCP 서버 오류] " + e.getMessage());
        }
    }

    public void remove(ClientManager cm) {
        clients.remove(cm);
    }
}
