package roomescape.service;

public class InvalidAuthenticationException extends RuntimeException {

    public InvalidAuthenticationException() {
        super("로그인에 실패했습니다.");
    }
}
