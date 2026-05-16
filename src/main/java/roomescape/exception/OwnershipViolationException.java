package roomescape.exception;

public class OwnershipViolationException extends ApiException {
    public OwnershipViolationException(ErrorCode code, String message) {
        super(code, message);
    }
}
