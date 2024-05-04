package roomescape.exception;

public class EmptyDataAccessException extends RuntimeException {
    public EmptyDataAccessException(String message, Object... args) {
        super(String.format(message, args));
    }
}
