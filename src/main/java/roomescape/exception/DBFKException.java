package roomescape.exception;

public class DBFKException extends RuntimeException {

    public DBFKException(String message, Exception e) {
        super(message, e);
    }
}
