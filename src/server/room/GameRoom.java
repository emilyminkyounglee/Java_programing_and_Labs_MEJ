package server.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import protocol.MessageEnvelope;
import server.tcp.ClientManager;

public class GameRoom {

    private final String roomId;
    private final List<ClientManager> players =
            Collections.synchronizedList(new ArrayList<>());

    public GameRoom(String id) {
        this.roomId = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public List<ClientManager> getPlayers() {
        return players;
    }

    public void addPlayer(ClientManager client) {
        if (!players.contains(client)) {
            players.add(client);
            System.out.println("[방 참가] room=" + roomId + ", user=" + client.getUserId());
        }
    }

    public void removePlayer(ClientManager client) {
        players.remove(client);
        System.out.println("[방 퇴장] room=" + roomId + ", user=" + client.getUserId());
    }

    public void broadcast(MessageEnvelope env) {
        synchronized (players) {
            for (ClientManager c : players) {
                try {
                    c.send(env);
                } catch (Exception e) {
                    System.err.println("[방송 실패] user=" + c.getUserId());
                }
            }
        }
    }
}
