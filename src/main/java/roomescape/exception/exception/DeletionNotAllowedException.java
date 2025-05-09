package roomescape.exception.exception;

public class DeletionNotAllowedException extends RuntimeException {

    public DeletionNotAllowedException(final String message) {
        super(message);
    }
}
