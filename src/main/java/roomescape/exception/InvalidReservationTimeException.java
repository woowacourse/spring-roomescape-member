package roomescape.exception;

public class InvalidReservationTimeException extends RuntimeException {

    public InvalidReservationTimeException(final String message) {
        super(message);
    }
}
