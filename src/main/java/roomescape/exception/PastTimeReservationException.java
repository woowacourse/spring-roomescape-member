package roomescape.exception;

public class PastTimeReservationException extends RuntimeException {
    public PastTimeReservationException(String message) {
        super(message);
    }
}
