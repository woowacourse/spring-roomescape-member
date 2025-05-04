package roomescape.exception;

public class LoginFailException extends RootException {

    private static final String MESSAGE = "로그인에 실패하였습니다.";

    public LoginFailException() {
        super(MESSAGE);
    }
}
