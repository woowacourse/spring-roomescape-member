package roomescape.domain.exception;

import roomescape.exception.CustomException;

public class InvalidNameException extends CustomException {

    public InvalidNameException(final String message) {
        super(message);
    }
}
