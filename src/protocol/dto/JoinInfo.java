package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//JOIN 메시지 (클라 -> 서버)
//

public class JoinInfo implements Serializable {
    private static final long serialVersionUID = 2L;

    private String nickname;
    private String password; // 선택
    private UUID userId;     // 서버가 채움

    public JoinInfo() {}
    public JoinInfo(String nickname, String password) {
        this.nickname = nickname; this.password = password;
    }
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public UUID getUserId() { return userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPassword(String password) { this.password = password; }
    public void setUserId(UUID userId) { this.userId = userId; }
}
