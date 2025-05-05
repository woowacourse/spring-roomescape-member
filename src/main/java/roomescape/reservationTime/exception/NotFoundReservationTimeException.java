package roomescape.reservationTime.exception;

import roomescape.globalException.NotFoundException;

public class NotFoundReservationTimeException extends NotFoundException {

    public NotFoundReservationTimeException(String message) {
        super(message);
    }
}
