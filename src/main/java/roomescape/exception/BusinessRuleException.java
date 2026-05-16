package roomescape.exception;

public class BusinessRuleException extends ApiException {
    public BusinessRuleException(ErrorCode code, String message) {
        super(code, message);
    }
}
