package roomescape.time.domain.exception;

import roomescape.common.exception.NotFoundException;

public class ReservationTimeNotFoundException extends NotFoundException {
    public ReservationTimeNotFoundException(String message) {
        super(message);
    }
}
