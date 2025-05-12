package roomescape.global.exception.impl;

import roomescape.global.exception.RoomescapeException;

public class BadRequestException extends RoomescapeException {
    public BadRequestException(String message) {
        super(message);
    }
}
