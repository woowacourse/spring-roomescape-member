package roomescape.member.application;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class RoleNotFoundException extends BusinessException {

    public RoleNotFoundException() {
        super(ErrorCode.ROLE_NOT_FOUND);
    }
}
