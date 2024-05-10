package roomescape.repository.exception;

import roomescape.exception.NotFoundException;

public class TimeNotFoundException extends NotFoundException {

    public TimeNotFoundException(final String message) {
        super(message);
    }
}
