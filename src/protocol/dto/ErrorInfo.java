package protocol.dto;

import java.io.Serializable;

//메시지 : 에러메시지   (서버 -> 클라)

// 사용 형식 : ErrorInfo(code, message)

public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 4L;

    private String code;     // ErrorType.name()
    private String message;

    public ErrorInfo() {}
    public ErrorInfo(String code, String message) { this.code = code; this.message = message; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public void setCode(String code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
}
