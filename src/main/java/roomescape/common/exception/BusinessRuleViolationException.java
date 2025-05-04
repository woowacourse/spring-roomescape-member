package roomescape.common.exception;

public class BusinessRuleViolationException extends CoreException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }

    public BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
