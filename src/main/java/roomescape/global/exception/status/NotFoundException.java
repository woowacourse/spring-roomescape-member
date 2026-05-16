package roomescape.global.exception.status;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public abstract class NotFoundException extends RoomescapeException {

    protected NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
