package client.state;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import protocol.GamePhase;

public class ClientState {
    public UUID userId;
    public String nickname;
    public UUID roomId;

    public volatile GamePhase phase = GamePhase.LOBBY;
    public volatile int secondsLeft = 0;

    // 체인별 받은 데이터(문장/이미지참조)
    private final Map<UUID, String> chainSentence = new ConcurrentHashMap<UUID, String>();
    private final Map<UUID, String> chainImageRef = new ConcurrentHashMap<UUID, String>();

    public synchronized void updatePhase(String phaseName, int seconds) {
        this.phase = GamePhase.valueOf(phaseName);
        this.secondsLeft = seconds;
    }

    public void putSentence(UUID chainId, String sentence) {
        chainSentence.put(chainId, sentence);
    }

    public void putImageRef(UUID chainId, String imageRef) {
        chainImageRef.put(chainId, imageRef);
    }

    public String getSentence(UUID chainId) {
        return chainSentence.get(chainId);
    }

    public String getImageRef(UUID chainId) {
        return chainImageRef.get(chainId);
    }
}

