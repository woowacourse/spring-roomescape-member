package roomescape.exception.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(final String message) {
        super(message);
    }
}
