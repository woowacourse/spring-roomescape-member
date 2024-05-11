package roomescape.service.time.exception;

import roomescape.exception.BadRequestException;

public class TimeUsedException extends BadRequestException {

    public TimeUsedException(String message) {
        super(message);
    }
}
