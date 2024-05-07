package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidRequestException extends BadRequestException {

    public InvalidRequestException(final String message) {
        super(message);
    }
}
