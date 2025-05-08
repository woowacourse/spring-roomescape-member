package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class MemberNotFoundException extends RoomescapeException {
    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
