package roomescape.exception.custom;

import roomescape.exception.ErrorMessage;

public class PastReservationTimeException extends RuntimeException {
    public PastReservationTimeException(String message) {
        super(message);
    }

    public PastReservationTimeException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
