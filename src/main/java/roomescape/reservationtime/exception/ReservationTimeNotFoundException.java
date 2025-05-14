package roomescape.reservationtime.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationTimeNotFoundException extends RoomescapeException {
    public ReservationTimeNotFoundException() {
        super(HttpStatus.NOT_FOUND, TimeErrorCode.TIME_NOT_FOUNT);
    }
}
