package roomescape.exception;

public class AuthenticationException extends RuntimeException {

    private static final String MESSAGE = "권한이 인증되지 않았습니다.";

    public AuthenticationException(final String message) {
        super(message);
    }
}
