package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidEmailException extends BadRequestException {

    public InvalidEmailException(String message) {
        super(message);
    }
}
