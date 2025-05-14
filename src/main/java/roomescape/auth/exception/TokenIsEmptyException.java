package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class TokenIsEmptyException extends RoomescapeException {
    public TokenIsEmptyException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.TOKEN_IS_EMPTY);
    }
}
