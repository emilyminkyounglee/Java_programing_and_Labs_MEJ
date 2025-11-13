package protocol.dto;

import java.io.Serializable;

//메시지 : SUBMIT_SENTENCE

//내용: text
//서버: GameInstance.submitInitialSentence(playerId, text)로 위임

public class SentenceRequest implements Serializable {
    private static final long serialVersionUID = 10L;

    private String text;

    public SentenceRequest() {}
    public SentenceRequest(String text) { this.text = text; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
