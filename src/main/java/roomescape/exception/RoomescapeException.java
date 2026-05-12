package roomescape.exception;

public class RoomescapeException extends RuntimeException {
    private final ErrorCode code;

    public RoomescapeException(ErrorCode code) {
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
