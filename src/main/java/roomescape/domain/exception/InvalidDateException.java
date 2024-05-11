package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidDateException extends BadRequestException {

    public InvalidDateException(String message) {
        super(message);
    }
}
