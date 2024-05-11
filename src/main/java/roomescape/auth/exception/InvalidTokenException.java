package roomescape.auth.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("올바르지 않은 토큰입니다.");
    }
}
