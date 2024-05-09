package roomescape.auth.exception;

public class IllegalTokenException extends AuthorizationException {
    public IllegalTokenException(String message) {
        super(message);
    }
}
