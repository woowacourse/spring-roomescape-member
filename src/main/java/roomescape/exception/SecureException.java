package roomescape.exception;

import lombok.Getter;

@Getter
public abstract class SecureException extends RuntimeException {

    private final String sensitiveInformation;

    public SecureException(
            String message,
            String sensitiveInformation
    ) {
        super(message);
        this.sensitiveInformation = sensitiveInformation;
    }

    public SecureException(
            String message,
            String sensitiveInformation,
            Throwable cause
    ) {
        super(message, cause);
        this.sensitiveInformation = sensitiveInformation;
    }
}
