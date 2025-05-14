package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class MemberRoleNotExistsException extends RoomescapeException {
    public MemberRoleNotExistsException() {
        super(HttpStatus.NOT_FOUND, MemberErrorCode.MEMBER_ROLE_NOT_EXISTS);
    }
}
