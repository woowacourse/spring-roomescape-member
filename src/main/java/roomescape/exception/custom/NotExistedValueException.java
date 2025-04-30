package roomescape.exception.custom;

public class NotExistedValueException extends RuntimeException {

    public NotExistedValueException() {
        super();
    }

    public NotExistedValueException(String message) {
        super(message);
    }
}
