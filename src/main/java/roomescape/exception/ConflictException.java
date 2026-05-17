package roomescape.exception;

public class ConflictException extends RoomescapeException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
