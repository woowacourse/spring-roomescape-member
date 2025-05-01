package roomescape.exception.impl;

import roomescape.exception.RoomescapeException;

public class PastReservationException extends RoomescapeException {
    public PastReservationException(String message) {
        super(message);
    }
}
