package roomescape.exception;

public class UnprocessableEntityException extends RoomescapeException {
    public UnprocessableEntityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
