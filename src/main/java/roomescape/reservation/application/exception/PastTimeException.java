package roomescape.reservation.application.exception;

public class PastTimeException extends RuntimeException {
    public PastTimeException(String message) {
        super(message);
    }
}
