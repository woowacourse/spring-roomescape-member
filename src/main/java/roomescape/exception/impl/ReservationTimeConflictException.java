package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class ReservationTimeConflictException extends RoomescapeException {
    public ReservationTimeConflictException(String message) {
        super(message);
    }
}
