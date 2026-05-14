package roomescape.reservation.exception;

import roomescape.global.exception.exception.BadRequestException;

public class ReservationPastDateException extends BadRequestException {
    public ReservationPastDateException(String message) {
        super(message);
    }
}
