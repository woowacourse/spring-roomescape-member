package roomescape.reservationtime.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationTimeFieldRequiredException extends RoomescapeException {
    public ReservationTimeFieldRequiredException() {
        super(HttpStatus.BAD_REQUEST, TimeErrorCode.TIME_FIELD_REQUIRED);
    }
}
