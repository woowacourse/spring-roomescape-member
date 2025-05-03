package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ReservationTimeIntervalException extends RootException {

    private static final String MESSAGE = "최소 시간 간격보다 작습니다.";

    public ReservationTimeIntervalException() {
        super(MESSAGE);
    }
}
