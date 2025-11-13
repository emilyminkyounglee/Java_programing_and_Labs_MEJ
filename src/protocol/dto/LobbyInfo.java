package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//메시지 : JOINED (서버 -> 클라)

public class LobbyInfo implements Serializable {
    private static final long serialVersionUID = 3L;

    private UUID roomId;
    private int players;
    private int neededPlayers;

    public LobbyInfo() {}
    public LobbyInfo(UUID roomId, int players, int neededPlayers) {
        this.roomId = roomId; this.players = players; this.neededPlayers = neededPlayers;
    }
    public UUID getRoomId() { return roomId; }
    public int getPlayers() { return players; }
    public int getNeededPlayers() { return neededPlayers; }
    public void setRoomId(UUID roomId) { this.roomId = roomId; }
    public void setPlayers(int players) { this.players = players; }
    public void setNeededPlayers(int neededPlayers) { this.neededPlayers = neededPlayers; }
}
