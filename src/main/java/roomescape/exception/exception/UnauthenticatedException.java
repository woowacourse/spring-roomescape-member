package roomescape.exception.exception;

public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {
        super();
    }

    public UnauthenticatedException(final String message) {
        super(message);
    }
}
