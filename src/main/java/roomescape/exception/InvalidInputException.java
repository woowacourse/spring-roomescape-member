package roomescape.exception;

public class InvalidInputException extends ApiException {
    public InvalidInputException(ErrorCode code, String message) {
        super(code, message);
    }
}
