package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ReservationBeforeStartException extends RootException {

    private static final String MESSAGE = "일주일 전부터 예약 가능합니다.";

    public ReservationBeforeStartException() {
        super(MESSAGE);
    }
}
