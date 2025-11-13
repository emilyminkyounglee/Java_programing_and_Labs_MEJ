package server.auth;

import java.util.HashMap;
import java.util.Map;
import server.tcp.ClientManager;

public class ClientRegistry {
	// id와 clientmanager 1:1 매칭
    private Map<String, ClientManager> session = new HashMap<>();

    public void addSession(ClientManager cm) {
        session.put(cm.getUserId(), cm);
        System.out.println("[클라이언트 등록] " + cm.getUserId());
    }

    public void removeSession(String userId) {
        if (userId == null) return;
        session.remove(userId);
        System.out.println("[클라이언트 제거] " + userId);
    }

    public ClientManager getSession(String userId) {
        return session.get(userId);
    }

    public boolean isOnline(String userId) {
        return session.containsKey(userId);
    }
}