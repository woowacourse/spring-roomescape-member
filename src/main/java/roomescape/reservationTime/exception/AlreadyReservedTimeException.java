package roomescape.reservationTime.exception;

import roomescape.globalException.BadRequestException;

public class AlreadyReservedTimeException extends BadRequestException {

    public AlreadyReservedTimeException(String message) {
        super(message);
    }
}
