package roomescape.global.exception.status;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public abstract class ConflictException extends RoomescapeException {

    protected ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
