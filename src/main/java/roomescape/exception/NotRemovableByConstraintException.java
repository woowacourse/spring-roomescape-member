package roomescape.exception;

public class NotRemovableByConstraintException extends BadRequestException{

    public NotRemovableByConstraintException() {}

    public NotRemovableByConstraintException(String message) {
        super(message);
    }
}
