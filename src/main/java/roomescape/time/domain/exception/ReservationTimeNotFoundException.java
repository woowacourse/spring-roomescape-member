package roomescape.time.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class ReservationTimeNotFoundException extends RoomescapeException {

    public ReservationTimeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
