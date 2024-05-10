package roomescape.domain.exception;

public class InvalidDomainObjectException extends RuntimeException {
    public InvalidDomainObjectException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
