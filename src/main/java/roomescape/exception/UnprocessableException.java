package roomescape.exception;

import roomescape.exception.code.UnprocessableCode;

public class UnprocessableException extends BaseException {

    public UnprocessableException(UnprocessableCode code) {
        super(code);
    }
}
