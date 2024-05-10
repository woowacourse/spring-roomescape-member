package roomescape.exception;

public class MemberAuthenticationException extends AuthenticationException {
    private static final String ERROR_MESSAGE = "인증이 되지 않은 유저입니다.";

    public MemberAuthenticationException() {
        super(ERROR_MESSAGE);
    }
}
