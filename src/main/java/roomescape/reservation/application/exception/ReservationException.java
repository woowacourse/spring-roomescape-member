package roomescape.reservation.application.exception;

import roomescape.exception.RoomEscapeException;

public class ReservationException extends RoomEscapeException {
    public ReservationException(String message) {
        super(message);
    }
}
