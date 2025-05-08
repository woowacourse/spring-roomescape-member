package roomescape.user.application;

import roomescape.common.BusinessException;
import roomescape.common.ErrorCode;

public class AuthorizationException extends BusinessException {

    public AuthorizationException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
