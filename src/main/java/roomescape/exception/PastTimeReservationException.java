package roomescape.exception;

public class PastTimeReservationException extends IllegalArgumentException {

    public PastTimeReservationException(String message) {
        super(message);
    }
}
