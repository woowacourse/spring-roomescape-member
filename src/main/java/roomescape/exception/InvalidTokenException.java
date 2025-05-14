package roomescape.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("필수 토큰이 존재하지 않습니다.");
    }
}
