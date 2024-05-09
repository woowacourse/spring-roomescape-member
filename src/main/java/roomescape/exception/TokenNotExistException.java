package roomescape.exception;

public class TokenNotExistException extends NotAuthenticatedException {
    public TokenNotExistException() {
        super("토큰이 존재하지 않습니다.");
    }
}
