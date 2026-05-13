package roomescape.exception;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message);
    }
}