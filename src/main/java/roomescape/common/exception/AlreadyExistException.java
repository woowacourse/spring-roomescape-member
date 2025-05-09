package roomescape.common.exception;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException() {
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
