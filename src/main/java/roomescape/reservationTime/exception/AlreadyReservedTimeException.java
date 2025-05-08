package roomescape.reservationTime.exception;

import roomescape.globalException.InvalidInputException;

public class AlreadyReservedTimeException extends InvalidInputException {

    public AlreadyReservedTimeException(String message) {
        super(message);
    }
}
