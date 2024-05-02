package roomescape.exception;

public class AlreadyExistsException extends IllegalArgumentException {

    AlreadyExistsException(final String message) {
        super(message);
    }
}
