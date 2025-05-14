package roomescape.auth.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("인증 과정에서 문제가 발생했습니다.");
    }

    public AuthorizationException(final String message) {
        super(message);
    }
}
