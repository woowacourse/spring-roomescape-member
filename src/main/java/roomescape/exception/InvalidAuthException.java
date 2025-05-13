package roomescape.exception;

public class InvalidAuthException extends RuntimeException {

    public InvalidAuthException(final String message) {
        super(message);
    }
}
