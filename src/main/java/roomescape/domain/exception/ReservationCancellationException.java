package roomescape.domain.exception;

import roomescape.common.exception.BusinessRuleViolationException;

public class ReservationCancellationException extends BusinessRuleViolationException {

    public ReservationCancellationException(final String message) {
        super(message);
    }
}
