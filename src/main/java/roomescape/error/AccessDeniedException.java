package roomescape.error;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(final String message) {
        super(message);
    }
}
