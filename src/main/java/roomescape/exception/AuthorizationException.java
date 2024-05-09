package roomescape.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("이메일 또는 비밀번호가 잘못되었습니다.");
    }
}
