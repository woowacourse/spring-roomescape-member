package roomescape.global.exception.validation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class InvalidNameLengthException extends ReservationException {

    public InvalidNameLengthException(String detail) {
        super(ErrorCode.INVALID_NAME_LENGTH, ErrorCode.INVALID_NAME_LENGTH.getMessage(), detail);
    }

    public InvalidNameLengthException() {
        super(ErrorCode.INVALID_NAME_LENGTH, ErrorCode.INVALID_NAME_LENGTH.getMessage());
    }
}
