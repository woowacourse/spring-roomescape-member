package roomescape.support.exception;

public class NotFoundException extends RoomescapeException {

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
