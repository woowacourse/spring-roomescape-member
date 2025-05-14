package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class UnauthorizedAccessException extends RoomescapeException {
    public UnauthorizedAccessException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.UNAUTHORIZED_ACCESS);
    }
}
