package roomescape.auth.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public static final class InvalidCredentialsException extends AuthenticationException {

        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
}
