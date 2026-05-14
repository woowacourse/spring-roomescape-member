package roomescape.exception.custom;

import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorMessage;

public class NotFoundException extends RoomescapeException {

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
    }
}
