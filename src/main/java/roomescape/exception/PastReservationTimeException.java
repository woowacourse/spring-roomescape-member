package roomescape.exception;

public class PastReservationTimeException extends RuntimeException {
    public PastReservationTimeException(String message) {
        super(message);
    }
}
