package roomescape.exception;

public class InvalidInputException extends ApiException {
    public InvalidInputException(String code, String message) {
        super(code, message);
    }
}