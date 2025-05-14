package roomescape.member.exception;

import roomescape.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {
        this("요청한 id와 일치하는 유저 정보가 없습니다.");
    }

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
