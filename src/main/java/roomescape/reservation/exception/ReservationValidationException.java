package roomescape.reservation.exception;

import java.util.List;
import roomescape.global.exception.exception.ValidationException;

public class ReservationValidationException extends ValidationException {
    public ReservationValidationException(List<String> errors) {
        super(errors);
    }
}
