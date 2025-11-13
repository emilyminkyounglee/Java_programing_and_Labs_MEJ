package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//메시지 : NEW_ROUND_DATA
//내용: chainId, kind(SENTENCE|DRAWING), value(문장 or 이미지참조)
//용도: “넌 다음 라운드에 이 체인에서 무엇을 해야 함”을 단일 DTO로 공지

public class NextTaskData implements Serializable {
    private static final long serialVersionUID = 25L;

    public enum Kind { SENTENCE, DRAWING }

    private UUID chainId;
    private Kind kind;
    private String value; // 문장 텍스트 or 이미지 경로/ID/URL

    public NextTaskData() {}
    public NextTaskData(UUID chainId, Kind kind, String value) {
        this.chainId = chainId; this.kind = kind; this.value = value;
    }
    public UUID getChainId() { return chainId; }
    public Kind getKind() { return kind; }
    public String getValue() { return value; }
    public void setChainId(UUID chainId) { this.chainId = chainId; }
    public void setKind(Kind kind) { this.kind = kind; }
    public void setValue(String value) { this.value = value; }
}
