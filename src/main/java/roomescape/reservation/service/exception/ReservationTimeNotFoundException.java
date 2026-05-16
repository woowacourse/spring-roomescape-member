package roomescape.reservation.service.exception;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.code.RoomEscapeErrorCode;

public class ReservationTimeNotFoundException extends RoomEscapeException {
    public ReservationTimeNotFoundException(RoomEscapeErrorCode roomEscapeErrorCode) {
        super(roomEscapeErrorCode);
    }
}
