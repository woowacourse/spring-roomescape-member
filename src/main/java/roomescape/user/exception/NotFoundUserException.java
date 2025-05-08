package roomescape.user.exception;

import roomescape.globalException.NotFoundException;

public class NotFoundUserException extends NotFoundException {

    public NotFoundUserException(String message) {
        super(message);
    }
}
