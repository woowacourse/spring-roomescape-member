package roomescape.auth.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
    }

    public AuthorizationException(final String message) {
        super(message);
    }
}
