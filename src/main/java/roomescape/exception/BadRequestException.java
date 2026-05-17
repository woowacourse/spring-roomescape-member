package roomescape.exception;

public class BadRequestException extends RoomescapeException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
