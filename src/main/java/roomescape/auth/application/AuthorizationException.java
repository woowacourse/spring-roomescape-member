package roomescape.auth.application;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class AuthorizationException extends BusinessException {

    public AuthorizationException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
