package roomescape.reservation.domain.exception;

import roomescape.global.exception.RoomEscapeException;

public class InvalidReservationException extends RoomEscapeException {
    public InvalidReservationException(String message) {
        super(message);
    }
}
