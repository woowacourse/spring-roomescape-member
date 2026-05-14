package roomescape.exception.custom;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorMessage;

public class BadRequestException extends RoomescapeException {

    public BadRequestException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
