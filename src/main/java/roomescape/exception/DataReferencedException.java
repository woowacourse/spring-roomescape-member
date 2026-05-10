package roomescape.exception;

public class DataReferencedException extends SecureException {

    public DataReferencedException(
            String message,
            String sensitiveInformation,
            Throwable cause
    ) {
        super(message, sensitiveInformation, cause);
    }
}
