package roomescape.exception;

public class EmptyValueException extends RuntimeException {

    public EmptyValueException(ExceptionCause exception) {
        super(exception.getMessage());
    }
}
