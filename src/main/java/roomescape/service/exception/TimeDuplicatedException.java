package roomescape.service.exception;

import roomescape.exception.DuplicateException;

public class TimeDuplicatedException extends DuplicateException {

    public TimeDuplicatedException(String message) {
        super(message);
    }
}
