package roomescape.exception;

public class IllegalTokenException extends RuntimeException {
    private final String message;

    public IllegalTokenException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
