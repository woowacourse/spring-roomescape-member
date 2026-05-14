package roomescape.global.exception.reservation;

import roomescape.global.exception.status.BadRequestException;

public class InvalidReservationException extends BadRequestException {

    public InvalidReservationException(String message) {
        super(message);
    }
}
