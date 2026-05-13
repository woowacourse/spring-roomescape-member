package roomescape.reservation.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class PastReservationException extends RoomescapeException {

    public PastReservationException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
