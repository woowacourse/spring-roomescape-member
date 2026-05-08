package roomescape.exception.exception;

public abstract class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
