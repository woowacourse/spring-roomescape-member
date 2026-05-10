package roomescape.reservationtime.domain.exception;

import roomescape.global.RoomEscapeException;

public class InvalidReservationTimeException extends RoomEscapeException {
    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
