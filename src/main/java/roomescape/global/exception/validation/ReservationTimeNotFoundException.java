package roomescape.global.exception.validation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class ReservationTimeNotFoundException extends ReservationException {

    public ReservationTimeNotFoundException() {
        super(ErrorCode.TIME_NOT_FOUND, ErrorCode.TIME_NOT_FOUND.getMessage());
    }
}
