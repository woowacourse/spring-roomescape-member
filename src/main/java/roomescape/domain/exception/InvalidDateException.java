package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidDateException extends BadRequestException {

    public InvalidDateException(final String message) {
        super(message);
    }
}
