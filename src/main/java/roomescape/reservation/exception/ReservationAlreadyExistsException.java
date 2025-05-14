package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ReservationAlreadyExistsException extends RoomescapeException {

    public ReservationAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, ReservationErrorCode.RESERVATION_ALREADY_EXISTS);
    }
}
