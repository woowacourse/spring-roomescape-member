package roomescape.service.exception;

import roomescape.exception.RoomescapeException;

public class MemberNotFoundException extends RoomescapeException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
