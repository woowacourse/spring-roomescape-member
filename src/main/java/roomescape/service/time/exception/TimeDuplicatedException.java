package roomescape.service.time.exception;

import roomescape.exception.ConflictException;

public class TimeDuplicatedException extends ConflictException {

    public TimeDuplicatedException(String message) {
        super(message);
    }
}
