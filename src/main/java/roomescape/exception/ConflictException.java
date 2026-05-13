package roomescape.exception;

import roomescape.exception.code.ConflictCode;

public class ConflictException extends BaseException {

    public ConflictException(ConflictCode code) {
        super(code);
    }
}
