package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class DuplicateTimeException extends BusinessException {

    public DuplicateTimeException() {
        super(TimeErrorPolicy.DUPLICATE_TIME);
    }
}
