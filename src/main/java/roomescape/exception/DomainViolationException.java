package roomescape.exception;

public class DomainViolationException extends RuntimeException {

    public DomainViolationException(String message) {
        super(message);
    }
}
