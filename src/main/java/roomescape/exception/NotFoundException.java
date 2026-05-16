package roomescape.exception;

public class NotFoundException extends ApiException {
    public NotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}
