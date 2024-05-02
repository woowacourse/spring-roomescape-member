package roomescape.domain.exception;

import roomescape.exception.CustomException;

public class InvalidRequestException extends CustomException {

    public InvalidRequestException(final String message) {
        super(message);
    }
}
