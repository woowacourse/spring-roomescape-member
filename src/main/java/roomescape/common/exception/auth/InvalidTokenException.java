package roomescape.common.exception.auth;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(final String message) {
        super(message);
    }
}
