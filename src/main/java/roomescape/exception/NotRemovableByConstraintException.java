package roomescape.exception;

public class NotRemovableByConstraintException extends RuntimeException{

    public NotRemovableByConstraintException() {}

    public NotRemovableByConstraintException(String message) {
        super(message);
    }
}
