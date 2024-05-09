package roomescape.exception;

public class AuthorizationException extends IllegalArgumentException{
    public AuthorizationException() {
        super("토큰이 없습니다.");
    }
}
