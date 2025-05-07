package roomescape.domain.auth.exception;

public class InvalidAuthorizationException extends RuntimeException {
    public InvalidAuthorizationException(final String message) {
        super(message);
    }
}
