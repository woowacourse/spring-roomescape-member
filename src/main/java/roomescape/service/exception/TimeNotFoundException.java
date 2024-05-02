package roomescape.service.exception;

import roomescape.exception.CustomException;

public class TimeNotFoundException extends CustomException {

    public TimeNotFoundException(final String message) {
        super(message);
    }
}
