package roomescape.member.application;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class InvalidRoleException extends BusinessException {

    public InvalidRoleException() {
        super(ErrorCode.INVALID_ROLE);
    }
}
