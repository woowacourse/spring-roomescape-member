package roomescape.auth.exception;

import roomescape.global.exception.CustomException;
import roomescape.global.exception.ErrorCode;

public class AuthorizationException extends CustomException {
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode,"인증 오류");
    }
}
