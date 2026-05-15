package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ReservationDeadlineException extends RoomEscapeException {

    public ReservationDeadlineException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
