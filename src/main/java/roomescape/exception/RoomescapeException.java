package roomescape.exception;

public class RoomescapeException extends RuntimeException {
    private final ErrorCode errorCode;

    public RoomescapeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
