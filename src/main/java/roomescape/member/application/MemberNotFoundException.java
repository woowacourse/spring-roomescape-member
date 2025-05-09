package roomescape.member.application;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
