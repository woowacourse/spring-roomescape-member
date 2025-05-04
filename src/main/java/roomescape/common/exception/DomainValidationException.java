package roomescape.common.exception;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException() {
    }

    public DomainValidationException(String message) {
        super(message);
    }
}
