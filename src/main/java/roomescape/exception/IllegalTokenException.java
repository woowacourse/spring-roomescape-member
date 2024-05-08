package roomescape.exception;

public class IllegalTokenException extends NotAuthenticatedException {
    public IllegalTokenException(String message) {
        super(message);
    }
}
