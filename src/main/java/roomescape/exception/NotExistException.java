package roomescape.exception;

public class NotExistException extends IllegalArgumentException {

    public NotExistException(final String message) {
        super(message);
    }
}
