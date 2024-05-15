package roomescape.domain.exception;

import roomescape.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
