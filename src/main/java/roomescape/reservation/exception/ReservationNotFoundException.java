package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationNotFoundException extends RoomescapeException {
    public ReservationNotFoundException() {
        super(HttpStatus.NOT_FOUND, ReservationErrorCode.RESERVATION_NOT_FOUND);
    }
}
