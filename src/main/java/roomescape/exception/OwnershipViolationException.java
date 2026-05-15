package roomescape.exception;

public class OwnershipViolationException extends ApiException {
    public OwnershipViolationException(String code, String message) {
        super(code, message);
    }
}
