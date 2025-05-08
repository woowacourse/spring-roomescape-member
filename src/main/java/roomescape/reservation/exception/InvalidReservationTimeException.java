package roomescape.reservation.exception;

import roomescape.globalException.InvalidInputException;

public class InvalidReservationTimeException extends InvalidInputException {

    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
