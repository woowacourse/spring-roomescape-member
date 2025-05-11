package roomescape.exception.auth;

public class LoginFailException extends RuntimeException {

    private static final String MESSAGE = "로그인에 실패하였습니다.";

    public LoginFailException() {
        super(MESSAGE);
    }

    public LoginFailException(String message) {
        super(message);
    }
}
