package roomescape.reservation.exception;

import roomescape.global.exception.exception.InvalidUserInputException;

public class ReservationPastDateException extends InvalidUserInputException {
    public ReservationPastDateException(String message) {
        super(message);
    }
}
