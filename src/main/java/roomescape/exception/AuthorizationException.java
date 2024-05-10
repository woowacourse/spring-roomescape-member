package roomescape.exception;

public class AuthorizationException extends IllegalArgumentException{
    private static final AuthorizationException SINGLETON = new AuthorizationException();
    public static AuthorizationException getInstance() {
        return SINGLETON;
    }
    private AuthorizationException() {
        super("토큰이 없습니다.");
    }
}
