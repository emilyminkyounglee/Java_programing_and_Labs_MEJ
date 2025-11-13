package protocol;

//실패 이유에 대한 메시지

public enum ErrorType {
    UNAUTHORIZED,
    PHASE_INVALID,
    NOT_ASSIGNED,
    DUPLICATE_SUBMISSION,
    CHAIN_MISMATCH,
    BAD_REQUEST
}


//사용 형식
//new ErrorInfo(ErrorType.PHASE_INVALID.name(), "지금은 추리 단계가 아닙니다")
//=> ErrorInfo() 내부에서 사용됨