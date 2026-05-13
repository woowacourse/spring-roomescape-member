package roomescape.exception;

import roomescape.exception.code.BadRequestCode;

public class BadRequestException extends BaseException {

    public BadRequestException(BadRequestCode code) {
        super(code);
    }
}
