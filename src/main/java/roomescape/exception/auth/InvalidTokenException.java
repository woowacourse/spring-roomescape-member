package roomescape.exception.auth;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("잘못된 토큰입니다.");
    }

    public InvalidTokenException(final String message) {
        super(message);
    }
}
