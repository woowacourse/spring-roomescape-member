package roomescape.exception;

public class InvalidInputException extends CustomException {
    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode);
    }
}
