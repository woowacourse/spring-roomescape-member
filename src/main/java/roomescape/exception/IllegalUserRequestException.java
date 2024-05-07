package roomescape.exception;

public class IllegalUserRequestException extends RuntimeException {

    public IllegalUserRequestException() {
    }

    public IllegalUserRequestException(String message) {
        super(message);
    }

    public IllegalUserRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalUserRequestException(Throwable cause) {
        super(cause);
    }

    public IllegalUserRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
