package roomescape.exception.auth;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("아이디 또는 비밀번호가 잘못되었습니다.");
    }

    public UnauthorizedAccessException(final String message) {
        super(message);
    }
}
