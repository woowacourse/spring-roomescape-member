package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationFieldRequiredException extends RoomescapeException {
    public ReservationFieldRequiredException() {
        super(HttpStatus.BAD_REQUEST, ReservationErrorCode.RESERVATION_FIELD_REQUIRED);
    }
}
