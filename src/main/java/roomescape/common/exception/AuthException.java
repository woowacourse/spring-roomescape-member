package roomescape.common.exception;

public class AuthException extends CoreException {
    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class LoginAuthException extends AuthException {

        public LoginAuthException(String message) {
            super(message);
        }

        public LoginAuthException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
