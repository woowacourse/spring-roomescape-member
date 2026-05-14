package roomescape.global.exception.reservation;

import roomescape.global.exception.status.BadRequestException;

public class InvalidReservationPagingException extends BadRequestException {

    public InvalidReservationPagingException(String message) {
        super(message);
    }
}
