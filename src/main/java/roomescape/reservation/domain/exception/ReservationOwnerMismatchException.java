package roomescape.reservation.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class ReservationOwnerMismatchException extends RoomescapeException {

    public ReservationOwnerMismatchException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
