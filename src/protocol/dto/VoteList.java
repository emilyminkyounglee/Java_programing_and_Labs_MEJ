package protocol.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

//메시지 : VOTING_PHASE_STARTED (서버 -> 클라)

//내용: 여러 체인에 대한 (chainId, initialSentence, finalGuess, guesserId) 리스트
//클라: 각 항목에 대해 O/X UI 렌더

public class VoteList implements Serializable {
    private static final long serialVersionUID = 26L;

    public static class Item implements Serializable {
        private static final long serialVersionUID = 26_1L;
        public UUID chainId;
        public String initialSentence;
        public String finalGuess;
        public UUID guesserId; // 이 체인의 추리자(투표 제외)
        public Item() {}
        public Item(UUID chainId, String initialSentence, String finalGuess, UUID guesserId) {
            this.chainId = chainId; this.initialSentence = initialSentence;
            this.finalGuess = finalGuess; this.guesserId = guesserId;
        }
    }

    private List<Item> items;

    public VoteList() {}
    public VoteList(List<Item> items) { this.items = items; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
