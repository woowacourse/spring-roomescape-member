package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ReservationBeforeStartException extends RootBusinessException {

    private static final String MESSAGE = "일주일 전부터 예약 가능합니다.";

    public ReservationBeforeStartException() {
        super(MESSAGE);
    }
}
