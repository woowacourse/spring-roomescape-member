package roomescape.web.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("로그인이 필요합니다.");
    }
}
