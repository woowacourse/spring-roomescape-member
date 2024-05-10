package roomescape.exception.auth;

import org.springframework.http.HttpStatus;
import roomescape.exception.RoomescapeException;

public class UnauthorizedPasswordException extends RoomescapeException {
    public UnauthorizedPasswordException() {
        super("비밀번호가 틀립니다.", HttpStatus.UNAUTHORIZED);
    }
}
