package server.room;

import java.util.HashMap;
import java.util.Map;
import server.tcp.ClientManager;

public class RoomRegistry {
	private static Map<String, GameRoom> roomMap = new HashMap<>();

	public static void joinRoom(String id, ClientManager client) {
		GameRoom room = roomMap.get(id);
		if (room == null) {
			room = new GameRoom(id);
			roomMap.put(id, room);
			System.out.println("새 방 생성됨: " + id);
		}
		room.addPlayer(client);
	}
    
    public static GameRoom getRoom(String roomId) {
        return roomMap.get(roomId);
    }
}