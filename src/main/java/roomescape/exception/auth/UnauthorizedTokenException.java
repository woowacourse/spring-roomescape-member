package roomescape.exception.auth;

import org.springframework.http.HttpStatus;
import roomescape.exception.RoomescapeException;

public class UnauthorizedTokenException extends RoomescapeException {
    public UnauthorizedTokenException() {
        super("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
