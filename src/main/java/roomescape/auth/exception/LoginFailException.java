package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class LoginFailException extends RoomescapeException {
    public LoginFailException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.LOGIN_FAILED);
    }
}
