package roomescape.service.auth;

public class AuthenticationFailException extends IllegalArgumentException {

    public AuthenticationFailException() {
        super("올바른 인증 정보를 입력해주세요.");
    }
}
