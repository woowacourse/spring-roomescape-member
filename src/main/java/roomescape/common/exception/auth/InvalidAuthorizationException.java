package roomescape.common.exception.auth;

public class InvalidAuthorizationException extends RuntimeException {

    public InvalidAuthorizationException(final String message) {
        super(message);
    }
}
