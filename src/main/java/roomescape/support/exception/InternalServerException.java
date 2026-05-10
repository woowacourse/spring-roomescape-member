package roomescape.support.exception;

public class InternalServerException extends RoomescapeException {

    public InternalServerException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
