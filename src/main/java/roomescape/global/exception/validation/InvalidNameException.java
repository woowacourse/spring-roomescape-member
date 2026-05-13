package roomescape.global.exception.validation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class InvalidNameException extends ReservationException {

    public InvalidNameException() {
        super(ErrorCode.INVALID_NAME, ErrorCode.INVALID_NAME.getMessage());
    }
}
