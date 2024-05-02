package roomescape.domain.exception;

import roomescape.exception.CustomException;

public class InvalidTimeException extends CustomException {

    public InvalidTimeException(final String message) {
        super(message);
    }
}
