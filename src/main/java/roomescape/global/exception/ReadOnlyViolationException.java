package roomescape.global.exception;

public class ReadOnlyViolationException extends RuntimeException {
    public ReadOnlyViolationException(String message) {
        super(message);
    }
}
