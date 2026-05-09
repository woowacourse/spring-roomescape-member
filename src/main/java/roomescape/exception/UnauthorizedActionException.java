package roomescape.exception;

public class UnauthorizedActionException extends ApiException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
