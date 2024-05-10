package roomescape.domain.exception;

public class AuthFailException extends RuntimeException {
    public AuthFailException() {
        super("로그인 정보가 잘못되었습니다.");
    }

    public AuthFailException(String s) {
        super(s);
    }
}
