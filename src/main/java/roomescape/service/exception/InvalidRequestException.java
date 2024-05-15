package roomescape.service.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.RoomescapeException;

public class InvalidRequestException extends RoomescapeException {

    public InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

