package roomescape.domain.exception;

public class InvalidDomainObjectException extends RuntimeException {
    public InvalidDomainObjectException(String message) {
        super(message);
    }
}
