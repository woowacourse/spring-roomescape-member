package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationInPastException extends RoomescapeException {
    public ReservationInPastException() {
        super(HttpStatus.BAD_REQUEST, ReservationErrorCode.RESERVATION_IN_PAST);
    }
}
