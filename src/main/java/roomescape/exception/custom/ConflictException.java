package roomescape.exception.custom;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorMessage;

public class ConflictException extends RoomescapeException {

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage(), HttpStatus.CONFLICT);
    }
}
