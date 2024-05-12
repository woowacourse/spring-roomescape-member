package roomescape.auth.exception;

public class AdminAuthenticationException extends AuthenticationException {
    private static final String ERROR_MESSAGE = "관리자만 접근할 수 있습니다.";

    public AdminAuthenticationException() {
        super(ERROR_MESSAGE);
    }
}
