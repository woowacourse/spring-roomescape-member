package roomescape.exception;

public class AlreadyExistsException extends IllegalArgumentException {

    AlreadyExistsException(String message) {
        super(message);
    }
}
