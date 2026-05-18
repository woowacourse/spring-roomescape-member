package roomescape.exception;

public class DomainConflictException extends DomainRuleViolationException {

    public DomainConflictException(String message) {
        super(message);
    }
}
