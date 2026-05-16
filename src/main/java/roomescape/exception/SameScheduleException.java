package roomescape.exception;

import org.springframework.http.HttpStatus;

public class SameScheduleException extends RoomEscapeException {

    public SameScheduleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}