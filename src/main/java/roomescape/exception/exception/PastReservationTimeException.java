package roomescape.exception.exception;

public class PastReservationTimeException extends RuntimeException {

    public PastReservationTimeException(final String message) {
        super(message);
    }
}
