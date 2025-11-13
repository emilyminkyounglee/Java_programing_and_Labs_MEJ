package protocol;

//게임의 큰 흐름
//OBBY → INIT_SENTENCE → DRAW_PHASE → GUESS_PHASE → VOTE_PHASE → END


public enum GamePhase {
    LOBBY,          // 카운트다운
    INIT_SENTENCE,  // 문장 입력
    DRAW_PHASE,     // 그림(UDP)
    GUESS_PHASE,    // 추리
    VOTE_PHASE,     // 투표
    END             // 종료
}