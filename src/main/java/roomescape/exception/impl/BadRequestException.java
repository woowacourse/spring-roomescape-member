package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class BadRequestException extends RoomescapeException {
    public BadRequestException(String message) {
        super(message);
    }
}
