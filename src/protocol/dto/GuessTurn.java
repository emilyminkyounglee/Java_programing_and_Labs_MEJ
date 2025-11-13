package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//메시지: YOUR_TURN_TO_GUESS (서버-> 클라)

//내용: chainId, imageUrl(또는 이미지 참조)
//클라: 이미지를 보고 추리 텍스트 입력 폼 오픈

public class GuessTurn implements Serializable {
    private static final long serialVersionUID = 24L;

    private UUID chainId;
    private String imageUrl;

    public GuessTurn() {}
    public GuessTurn(UUID chainId, String imageUrl) {
        this.chainId = chainId; this.imageUrl = imageUrl;
    }
    public UUID getChainId() { return chainId; }
    public String getImageUrl() { return imageUrl; }
    public void setChainId(UUID chainId) { this.chainId = chainId; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
