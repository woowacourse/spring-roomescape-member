package roomescape.exception;

public class AlreadyExistReservationException extends IllegalArgumentException {
    public AlreadyExistReservationException(String message) {
        super(message);
    }
}
