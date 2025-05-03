package roomescape.common.exception;

public class NotAbleReservationException extends RuntimeException {
    public NotAbleReservationException() {
    }

    public NotAbleReservationException(String message) {
        super(message);
    }
}
