package roomescape.domain.exception;

import roomescape.exception.CustomException;

public class InvalidDateException extends CustomException {

    public InvalidDateException(final String message) {
        super(message);
    }
}
