package roomescape.domain.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.RoomescapeException;

public class InvalidValueException extends RoomescapeException {

    public InvalidValueException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
