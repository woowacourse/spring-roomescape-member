package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class CookieNullException extends RoomescapeException {
    public CookieNullException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.COOKIE_NULL);
    }
}
