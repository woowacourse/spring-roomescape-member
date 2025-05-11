package roomescape.user.exception;

import roomescape.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
