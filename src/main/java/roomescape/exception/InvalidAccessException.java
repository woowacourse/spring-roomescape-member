package roomescape.exception;

public class InvalidAccessException  extends RuntimeException{

    public InvalidAccessException(String message, Throwable err) {
        super(message, err);
    }

    public InvalidAccessException(String message) {
        this(message, null);
    }
}
