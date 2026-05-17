package roomescape.reservation.service.exception;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.code.RoomEscapeErrorCode;

public class ReservationTimeCreateException extends RoomEscapeException{
    public ReservationTimeCreateException(RoomEscapeErrorCode roomEscapeErrorCode) {
        super(roomEscapeErrorCode);
    }
}
