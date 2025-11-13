package protocol;

import java.io.Serializable;

public class MessageEnvelope implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private Object body;  // protocol.dto.* 전용

    public MessageEnvelope() {}
    public MessageEnvelope(MessageType type, Object body) {
        this.type = type; this.body = body;
    }
    public MessageType getType() { return type; }
    public Object getBody() { return body; }
    public void setType(MessageType type) { this.type = type; }
    public void setBody(Object body) { this.body = body; }
}
