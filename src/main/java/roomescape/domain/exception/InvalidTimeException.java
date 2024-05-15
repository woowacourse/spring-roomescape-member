package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidTimeException extends BadRequestException {

    public InvalidTimeException(String message) {
        super(message);
    }
}
