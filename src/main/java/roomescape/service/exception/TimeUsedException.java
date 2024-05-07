package roomescape.service.exception;

import roomescape.exception.BadRequestException;

public class TimeUsedException extends BadRequestException {

    public TimeUsedException(final String message) {
        super(message);
    }
}
