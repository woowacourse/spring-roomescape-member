package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class TokenParseFailedException extends RoomescapeException {
    public TokenParseFailedException() {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.TOKEN_PARSE_FAILED);
    }
}
