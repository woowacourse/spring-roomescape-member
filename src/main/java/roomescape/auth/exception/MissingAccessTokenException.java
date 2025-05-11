package roomescape.auth.exception;

public class MissingAccessTokenException extends RuntimeException {

    public MissingAccessTokenException(final String message) {
        super(message);
    }
}
