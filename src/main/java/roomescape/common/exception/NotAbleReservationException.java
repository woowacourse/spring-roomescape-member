package roomescape.common.exception;

public class NotAbleReservationException extends InvalidRequestException {
    public NotAbleReservationException() {
    }

    public NotAbleReservationException(String message) {
        super(message);
    }
}
