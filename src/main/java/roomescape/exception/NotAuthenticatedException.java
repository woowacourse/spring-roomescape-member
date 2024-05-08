package roomescape.exception;

public class NotAuthenticatedException extends RuntimeException {
    private final String message;

    public NotAuthenticatedException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
