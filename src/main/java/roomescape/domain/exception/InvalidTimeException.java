package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidTimeException extends BadRequestException {

    public InvalidTimeException(final String message) {
        super(message);
    }
}
