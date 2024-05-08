package roomescape.exception;

public class TokenInvalidExpiredException extends RuntimeException {

    public TokenInvalidExpiredException(final String message) {
        super(message);
    }
}
