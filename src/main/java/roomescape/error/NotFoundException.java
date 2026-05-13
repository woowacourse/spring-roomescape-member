package roomescape.error;

public class NotFoundException extends RoomescapeException {
    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
