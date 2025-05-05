package roomescape.global.exception.impl;

import roomescape.global.exception.RoomescapeException;

public class NotFoundException extends RoomescapeException {
    public NotFoundException(String message) {
        super(message);
    }
}
