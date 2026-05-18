package roomescape.domain.exception;

public class DomainRuleViolationException extends DomainException {
    public DomainRuleViolationException(String message) {
        super(message);
    }
}
