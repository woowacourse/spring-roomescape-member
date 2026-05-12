package roomescape.time.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidTimeRequestException extends BusinessException {

    public InvalidTimeRequestException() {
        super(TimeErrorPolicy.INVALID_TIME_REQUEST);
    }
}
