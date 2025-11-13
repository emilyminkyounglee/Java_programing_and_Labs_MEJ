package protocol.dto;

import java.io.Serializable;

//메시지: PHASE_CHANGED (서버->클라)    #단계 전환

public class PhaseUpdate implements Serializable {
    private static final long serialVersionUID = 22L;

    private String phase;     // GamePhase.name()
    private int secondsLeft;  // 남은 시간

    public PhaseUpdate() {}
    public PhaseUpdate(String phase, int secondsLeft) {
        this.phase = phase; this.secondsLeft = secondsLeft;
    }
    public String getPhase() { return phase; }
    public int getSecondsLeft() { return secondsLeft; }
    public void setPhase(String phase) { this.phase = phase; }
    public void setSecondsLeft(int secondsLeft) { this.secondsLeft = secondsLeft; }
}
