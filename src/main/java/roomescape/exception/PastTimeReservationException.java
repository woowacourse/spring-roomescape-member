package roomescape.exception;

public class PastTimeReservationException extends IllegalArgumentException {

    public PastTimeReservationException(final String message) {
        super(message);
    }
}
