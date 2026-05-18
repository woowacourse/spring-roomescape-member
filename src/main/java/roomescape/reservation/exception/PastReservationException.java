package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;

import roomescape.error.BusinessException;
import roomescape.error.ErrorCode;

public class PastReservationException extends BusinessException {
    private PastReservationException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.PAST_RESERVATION);
    }

    public static PastReservationException pastReservation() {
        return new PastReservationException();
    }

    public static PastReservationException pastCancel() {
        return new PastReservationException();
    }

    public static PastReservationException pastUpdate() {
        return new PastReservationException();
    }
}
