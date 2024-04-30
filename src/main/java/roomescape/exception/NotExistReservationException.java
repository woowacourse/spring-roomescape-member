package roomescape.exception;

public class NotExistReservationException extends IllegalArgumentException {

    public NotExistReservationException(String message) {
        super(message);
    }
}
