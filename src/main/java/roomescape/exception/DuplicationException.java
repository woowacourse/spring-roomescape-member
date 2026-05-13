package roomescape.exception;

import roomescape.exception.code.ConflictCode;

public class DuplicationException extends BaseException {

    public DuplicationException(ConflictCode code) {
        super(code);
    }
}
