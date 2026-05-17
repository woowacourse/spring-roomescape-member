package roomescape.error;

public class RoomescapeException extends RuntimeException {
    private final ErrorCode errorCode;

    public RoomescapeException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public RoomescapeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
