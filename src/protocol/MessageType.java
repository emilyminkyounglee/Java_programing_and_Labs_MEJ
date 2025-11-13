package protocol;

//서버랑 클라이언트 간에 어떤 메시지를 주고받을 수 있는지 정하기
//명령어 목록
//TCP 이벤트의 종류를 표준화함

public enum MessageType {
	// Client -> Server
    JOIN,                 // 닉네임으로 참가
    SUBMIT_SENTENCE,      // 초기 문장 제출
    SUBMIT_GUESS,         // 추리 제출
    SUBMIT_VOTE,          // 투표 제출

    // Server -> Client
    JOINED,               // 로비 입장/인원 안내
    GAME_STARTED,         // 게임 시작 알림
    PHASE_CHANGED,        // 단계/남은 시간 방송
    YOUR_TURN_TO_DRAW,    // 내 그림 차례
    YOUR_TURN_TO_GUESS,   // 내 추리 차례
    NEW_ROUND_DATA,       // 다음 작업 안내(문장/그림)
    VOTING_PHASE_STARTED, // 투표 시작
    GAME_OVER,            // 결과/점수 방송
    ERROR                 // 에러 응답
    
}

//사용하는 형식
//new MessageEnvelope(MessageType.PHASE_CHANGED, new PhaseUpdate(...))
//=> MessageEnvelope() 내부에서 사용됨 + dto와 함꼐 사용됨




