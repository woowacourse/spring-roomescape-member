package roomescape.exception;

public class InvalidInputException extends CodeException {

    public InvalidInputException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
