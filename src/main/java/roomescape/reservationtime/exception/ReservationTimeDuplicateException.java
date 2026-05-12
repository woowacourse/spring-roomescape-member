package roomescape.reservationtime.exception;

import roomescape.global.exception.exception.DuplicateException;

public class ReservationTimeDuplicateException extends DuplicateException {

    public ReservationTimeDuplicateException() {
        super(ReservationTimeErrorCode.RESERVATION_TIME_DUPLICATE.getMessage());
    }
}
