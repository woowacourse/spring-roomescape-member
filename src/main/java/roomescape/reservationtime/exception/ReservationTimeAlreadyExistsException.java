package roomescape.reservationtime.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationTimeAlreadyExistsException extends RoomescapeException {
    public ReservationTimeAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, TimeErrorCode.TIME_ALREADY_EXISTS);
    }
}
