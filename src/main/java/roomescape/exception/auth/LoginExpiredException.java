package roomescape.exception.auth;

public class LoginExpiredException extends RuntimeException {

    private static final String MESSAGE = "로그인이 만료되었습니다.";

    public LoginExpiredException() {
        super(MESSAGE);
    }
}
