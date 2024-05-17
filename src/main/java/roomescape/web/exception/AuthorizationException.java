package roomescape.web.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("접근 권한이 필요합니다.");
    }
}
