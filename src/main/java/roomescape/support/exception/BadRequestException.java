package roomescape.support.exception;

public class BadRequestException extends RoomescapeException {

    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
