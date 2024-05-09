package roomescape.service.exception;

import roomescape.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
