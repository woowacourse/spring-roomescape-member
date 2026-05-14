package roomescape.global.exception.status;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public abstract class BadRequestException extends RoomescapeException {

    protected BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
