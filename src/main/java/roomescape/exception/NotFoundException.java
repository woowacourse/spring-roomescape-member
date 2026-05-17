package roomescape.exception;

public class NotFoundException extends RoomescapeException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
