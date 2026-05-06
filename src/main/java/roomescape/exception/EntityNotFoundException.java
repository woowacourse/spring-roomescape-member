package roomescape.exception;

public class EntityNotFoundException extends SecureException {

    public EntityNotFoundException(
            String message,
            String sensitiveInformation
    ) {
        super(message, sensitiveInformation);
    }
}
