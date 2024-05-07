package roomescape.exception;

public class AuthorizationException extends RuntimeException {

    private static final String MESSAGE = "권한이 인증되지 않았습니다.";

    public AuthorizationException() {
        this(MESSAGE);
    }

    public AuthorizationException(final String message) {
        super(message);
    }
}
