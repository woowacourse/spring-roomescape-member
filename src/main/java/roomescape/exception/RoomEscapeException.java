package roomescape.exception;

public class RoomEscapeException extends RuntimeException {

    private final ErrorCode code;

    public RoomEscapeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode;
    }

    public ErrorCode getCode() {
        return code;
    }
}
