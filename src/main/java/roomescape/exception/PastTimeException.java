package roomescape.exception;

import org.springframework.http.HttpStatus;

public class PastTimeException extends RoomescapeException {
    public PastTimeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
