package roomescape.reservation.application.exception;

public class DuplicateTimeException extends RuntimeException {
    public DuplicateTimeException(String message) {
        super(message);
    }
}
