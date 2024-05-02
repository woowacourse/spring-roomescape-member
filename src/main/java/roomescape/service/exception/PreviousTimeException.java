package roomescape.service.exception;

import roomescape.exception.CustomException;

public class PreviousTimeException extends CustomException {

    public PreviousTimeException(final String message) {
        super(message);
    }
}
