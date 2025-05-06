package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ReservationTimeIntervalException extends RootBusinessException {

    private static final String MESSAGE = "최소 시간 간격보다 작습니다.";

    public ReservationTimeIntervalException() {
        super(MESSAGE);
    }
}
