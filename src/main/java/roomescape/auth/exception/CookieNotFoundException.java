package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class CookieNotFoundException extends RoomescapeException {
    public CookieNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.COOKIE_NOT_FOUND);
    }
}
