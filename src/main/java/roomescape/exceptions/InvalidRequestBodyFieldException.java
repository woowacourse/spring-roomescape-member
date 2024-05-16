package roomescape.exceptions;

public class InvalidRequestBodyFieldException extends RuntimeException {

    public InvalidRequestBodyFieldException(String message) {
        super(message);
    }
}
