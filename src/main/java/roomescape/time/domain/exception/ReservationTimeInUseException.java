package roomescape.time.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.RoomescapeException;

public class ReservationTimeInUseException extends RoomescapeException {

    public ReservationTimeInUseException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
