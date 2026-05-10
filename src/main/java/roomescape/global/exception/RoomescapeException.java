package roomescape.global.exception;

public abstract class RoomescapeException extends RuntimeException {

    private final ErrorCode errorCode;

    protected RoomescapeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
