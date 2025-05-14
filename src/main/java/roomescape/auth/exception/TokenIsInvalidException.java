package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class TokenIsInvalidException extends RoomescapeException {
    public TokenIsInvalidException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.TOKEN_IS_INVALID);
    }
}
