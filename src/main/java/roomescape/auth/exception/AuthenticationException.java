package roomescape.auth.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("권한이 없습니다.");
    }
}
