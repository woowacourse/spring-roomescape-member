package roomescape.exception;

import org.springframework.http.HttpStatus;

public class SameNameException extends RoomEscapeException {

    public SameNameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
