package roomescape.reservation.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class ReservationNotFoundException extends RoomescapeException {

    public ReservationNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
