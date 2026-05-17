package roomescape.reservation.service.exception;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.code.RoomEscapeErrorCode;

public class ReservationTimeDeleteException extends RoomEscapeException{
    public ReservationTimeDeleteException(RoomEscapeErrorCode roomEscapeErrorCode) {
        super(roomEscapeErrorCode);
    }
}
