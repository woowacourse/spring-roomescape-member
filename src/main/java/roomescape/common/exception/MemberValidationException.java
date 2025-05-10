package roomescape.common.exception;

public class MemberValidationException extends DomainValidationException {
    public MemberValidationException() {
    }

    public MemberValidationException(String message) {
        super(message);
    }
}
