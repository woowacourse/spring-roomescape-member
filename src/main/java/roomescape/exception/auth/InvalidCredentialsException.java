package roomescape.exception.auth;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("아이디 또는 비밀번호가 잘못되었습니다.");
    }

    public InvalidCredentialsException(final String message) {
        super(message);
    }
}
