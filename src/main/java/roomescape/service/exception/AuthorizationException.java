package roomescape.service.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.RoomescapeException;

public class AuthorizationException extends RoomescapeException {

    public AuthorizationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
