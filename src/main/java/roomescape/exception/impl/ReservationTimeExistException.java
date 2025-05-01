package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class ReservationTimeExistException extends RoomescapeException {
    public ReservationTimeExistException(String message) {
        super(message);
    }
}
