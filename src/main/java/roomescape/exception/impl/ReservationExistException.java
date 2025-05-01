package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class ReservationExistException extends RoomescapeException {
    public ReservationExistException(String message) {
        super(message);
    }
}
