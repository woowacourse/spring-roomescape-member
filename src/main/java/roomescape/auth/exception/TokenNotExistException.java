package roomescape.auth.exception;

public class TokenNotExistException extends AuthorizationException {
    public TokenNotExistException() {
        super("토큰이 존재하지 않습니다.");
    }
}
