package roomescape.reservation.service.exception;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.code.RoomEscapeErrorCode;

public class ReservationChangeException extends RoomEscapeException {
    public ReservationChangeException(RoomEscapeErrorCode roomEscapeErrorCode) {
        super(roomEscapeErrorCode);
    }
}
