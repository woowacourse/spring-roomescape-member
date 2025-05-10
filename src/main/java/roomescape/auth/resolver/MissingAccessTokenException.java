package roomescape.auth.resolver;

public class MissingAccessTokenException extends RuntimeException {

    public MissingAccessTokenException(final String message) {
        super(message);
    }
}
