package roomescape.reservation.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class DuplicateReservationException extends RoomescapeException {

    public DuplicateReservationException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
