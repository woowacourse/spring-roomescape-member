package roomescape.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends RoomEscapeException {

    public AuthorizationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
