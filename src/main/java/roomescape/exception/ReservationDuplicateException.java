package roomescape.exception;

public class ReservationDuplicateException extends RuntimeException {

    public ReservationDuplicateException(final String message) {
        super(message);
    }
}
