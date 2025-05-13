package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class TokenIsExpiredException extends RoomescapeException {
    public TokenIsExpiredException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.TOKEN_IS_EXPIRED);
    }
}
