package roomescape.auth;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("권한이 없습니다.");
    }
}
