package roomescape.exception.custom;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorMessage;

public class PastReservationTimeException extends RoomescapeException {
    public PastReservationTimeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public PastReservationTimeException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
