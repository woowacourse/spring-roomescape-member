package roomescape.controller.member;

import roomescape.exception.RoomescapeException;

public class UnauthorizedException extends RoomescapeException {

    public UnauthorizedException(final String message) {
        super(message);
    }
}
