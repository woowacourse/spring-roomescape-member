package roomescape.domain.auth.exception;

public class UserNotFoundForTokenException extends RuntimeException {
    public UserNotFoundForTokenException(final String message) {
        super(message);
    }
}
