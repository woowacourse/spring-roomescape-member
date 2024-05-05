package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidNameException extends BadRequestException {

    public InvalidNameException(final String message) {
        super(message);
    }
}
