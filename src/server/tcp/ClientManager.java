package server.tcp;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import protocol.MessageEnvelope;

public class ClientManager implements Runnable {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String userId;
    private String nickname;
    private String roomId;

    private InetAddress udpAddress;
    private int udpPort = -1;

    private volatile boolean running = true;

    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            // 클라와 동일하게: 먼저 out, flush 후 in 생성
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Object 스트림 생성 실패", e);
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Object obj;
                try {
                    obj = in.readObject();
                } catch (EOFException | SocketException e) {
                    // 정상 종료로 간주
                    break;
                }

                if (!(obj instanceof MessageEnvelope)) {
                    System.err.println("알 수 없는 객체 수신: " + obj);
                    continue;
                }

                MessageEnvelope env = (MessageEnvelope) obj;
                MessageManager.manageMessage(env, this);
            }
        } catch (Exception e) {
            System.err.println("[클라이언트 처리 오류] " + e.getMessage());
        } finally {
            running = false;
            MessageManager.disconnected(this);
            closeQuietly();
            System.out.println("[클라이언트 종료] " + socket.getRemoteSocketAddress());
        }
    }

    public synchronized void send(MessageEnvelope env) {
        try {
            if (out == null) return;
            out.writeObject(env);
            out.flush();
        } catch (IOException e) {
            System.err.println("[TCP 전송 실패] " + e.getMessage());
        }
    }

    private void closeQuietly() {
        try { if (in != null) in.close(); } catch (Exception e) {}
        try { if (out != null) out.close(); } catch (Exception e) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (Exception e) {}
    }

    // ===== getter / setter =====

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setUdpEndpoint(InetAddress addr, int port) {
        this.udpAddress = addr;
        this.udpPort = port;
        System.out.printf("[UDP 주소 등록] %s -> %s:%d%n",
                userId, addr != null ? addr.getHostAddress() : "null", port);
    }

    public InetAddress getUdpAddress() {
        return udpAddress;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public boolean isUdpReady() {
        return udpPort != -1;
    }
}
