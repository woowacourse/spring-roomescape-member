package roomescape.domain.exception;

import roomescape.common.exception.BusinessRuleViolationException;

public class ReservationCancellationException extends BusinessRuleViolationException {

    public ReservationCancellationException() {
        super("예약일 이틀 전까지만 취소할 수 있습니다.");
    }
}
