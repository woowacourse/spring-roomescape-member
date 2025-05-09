package roomescape.user.application;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
