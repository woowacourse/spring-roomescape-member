package roomescape.exception;

public class NotAcceptableReservationException extends CodeException {

    public NotAcceptableReservationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
