package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidNameException extends BadRequestException {

    public InvalidNameException(String message) {
        super(message);
    }
}
