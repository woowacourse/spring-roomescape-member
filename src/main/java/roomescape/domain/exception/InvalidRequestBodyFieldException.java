package roomescape.domain.exception;

public class InvalidRequestBodyFieldException extends RuntimeException {

    public InvalidRequestBodyFieldException(String message) {
        super(message);
    }
}
