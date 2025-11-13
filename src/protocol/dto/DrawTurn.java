package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//메시지 : YOUR_TURN_TO_DRAW

//내용: chainId, sentence

public class DrawTurn implements Serializable {
    private static final long serialVersionUID = 23L;

    private UUID chainId;
    private String sentence;

    public DrawTurn() {}
    public DrawTurn(UUID chainId, String sentence) {
        this.chainId = chainId; this.sentence = sentence;
    }
    public UUID getChainId() { return chainId; }
    public String getSentence() { return sentence; }
    public void setChainId(UUID chainId) { this.chainId = chainId; }
    public void setSentence(String sentence) { this.sentence = sentence; }
}