package roomescape.application.support.exception;

public class CoreException extends RuntimeException {
    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
