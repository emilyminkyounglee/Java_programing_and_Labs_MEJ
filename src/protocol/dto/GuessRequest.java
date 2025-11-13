package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//메시지 : SUBMIT_GUESS
//내용: chainId, text

//서버: GameInstance.submitGuess(playerId, text, chainId)

public class GuessRequest implements Serializable {
    private static final long serialVersionUID = 11L;

    private UUID chainId;
    private String text;

    public GuessRequest() {}
    public GuessRequest(UUID chainId, String text) { this.chainId = chainId; this.text = text; }
    public UUID getChainId() { return chainId; }
    public String getText() { return text; }
    public void setChainId(UUID chainId) { this.chainId = chainId; }
    public void setText(String text) { this.text = text; }
}