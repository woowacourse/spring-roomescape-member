package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class EntityNotFoundException extends RoomescapeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
