package roomescape.exception.domain;

public class DomainConflictException extends DomainRuleViolationException {

    public DomainConflictException(String message) {
        super(message);
    }
}
