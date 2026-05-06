package roomescape.exception;

public class InUseEntityException extends SecureException {

    public InUseEntityException(
            String message,
            String sensitiveInformation,
            Throwable cause
    ) {
        super(message, sensitiveInformation, cause);
    }
}
