package roomescape.exception;

public class UnauthenticatedUserException extends RuntimeException {

    public UnauthenticatedUserException(final String message) {
        super(message);
    }
}
