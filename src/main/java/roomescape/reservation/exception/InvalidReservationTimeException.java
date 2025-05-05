package roomescape.reservation.exception;

import roomescape.globalException.BadRequestException;

public class InvalidReservationTimeException extends BadRequestException {

    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
