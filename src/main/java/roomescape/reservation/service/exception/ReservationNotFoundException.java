package roomescape.reservation.service.exception;

import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_NOT_FOUND;

import roomescape.exception.RoomEscapeException;

public class ReservationNotFoundException extends RoomEscapeException {
    public ReservationNotFoundException() {
        super(RESERVATION_NOT_FOUND);
    }
}
