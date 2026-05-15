package roomescape.exception;

public class BusinessRuleException extends ApiException {
    public BusinessRuleException(String code, String message) {
        super(code, message);
    }
}