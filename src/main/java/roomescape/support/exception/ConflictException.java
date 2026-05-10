package roomescape.support.exception;

public class ConflictException extends RoomescapeException {

    public ConflictException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
