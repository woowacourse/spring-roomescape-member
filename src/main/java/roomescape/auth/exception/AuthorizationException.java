package roomescape.auth.exception;

import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

public class AuthorizationException extends CustomException {
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode,"인증 오류");
    }
}
