package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidReservationException extends RoomEscapeException {
    public InvalidReservationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
