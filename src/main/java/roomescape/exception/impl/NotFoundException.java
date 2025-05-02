package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class NotFoundException extends RoomescapeException {
    public NotFoundException(String message) {
        super(message);
    }
}
