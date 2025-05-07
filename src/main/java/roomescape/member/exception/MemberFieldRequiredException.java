package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class MemberFieldRequiredException extends RoomescapeException {
    public MemberFieldRequiredException() {
        super(HttpStatus.BAD_REQUEST, MemberErrorCode.MEMBER_FIELD_REQUIRED);
    }
}
