package roomescape.user.application;

import roomescape.common.BusinessException;
import roomescape.common.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
