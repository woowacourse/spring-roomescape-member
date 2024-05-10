package roomescape.web.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends CustomException {
    public AuthorizationException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
