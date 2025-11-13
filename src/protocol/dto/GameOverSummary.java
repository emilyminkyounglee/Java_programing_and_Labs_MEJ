package protocol.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

//메시지 : GAME_OVER   (서버 -> 클라)

//내용: 체인별 결과(Result: 과반 통과 여부/득점자/점수) + yourTotalScore
//서버: 과반 판단→득점 반영→DBManager.saveScore(...)

public class GameOverSummary implements Serializable {
    private static final long serialVersionUID = 27L;

    public static class Result implements Serializable {
        private static final long serialVersionUID = 27_1L;
        public UUID chainId;
        public UUID ownerUserId;
        public UUID guesserUserId;
        public boolean passedMajority;
        public int gainedScore; // pass 시 guesser +10
        public Result() {}
        public Result(UUID chainId, UUID ownerUserId, UUID guesserUserId, boolean passedMajority, int gainedScore) {
            this.chainId = chainId; this.ownerUserId = ownerUserId;
            this.guesserUserId = guesserUserId; this.passedMajority = passedMajority; this.gainedScore = gainedScore;
        }
    }

    private List<Result> results;
    private int yourTotalScore;

    public GameOverSummary() {}
    public GameOverSummary(List<Result> results, int yourTotalScore) {
        this.results = results; this.yourTotalScore = yourTotalScore;
    }
    public List<Result> getResults() { return results; }
    public int getYourTotalScore() { return yourTotalScore; }
    public void setResults(List<Result> results) { this.results = results; }
    public void setYourTotalScore(int yourTotalScore) { this.yourTotalScore = yourTotalScore; }
}
