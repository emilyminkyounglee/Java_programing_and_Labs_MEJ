package protocol.dto;

import java.io.Serializable;

//메시지 : GAME_STARTED (서버->클라)

public class GameStartInfo implements Serializable {
    private static final long serialVersionUID = 20L;

    private int players;
    private int neededPlayers;

    public GameStartInfo() {}
    public GameStartInfo(int players, int neededPlayers) {
        this.players = players; this.neededPlayers = neededPlayers;
    }
    public int getPlayers() { return players; }
    public int getNeededPlayers() { return neededPlayers; }
    public void setPlayers(int players) { this.players = players; }
    public void setNeededPlayers(int neededPlayers) { this.neededPlayers = neededPlayers; }
}