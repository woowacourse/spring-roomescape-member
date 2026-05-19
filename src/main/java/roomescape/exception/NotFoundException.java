package roomescape.exception;

import roomescape.exception.code.NotFoundCode;

public class NotFoundException extends BaseException {

    public NotFoundException(NotFoundCode code) {
        super(code);
    }
}
