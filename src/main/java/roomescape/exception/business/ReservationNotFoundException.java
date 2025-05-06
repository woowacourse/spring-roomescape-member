package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ReservationNotFoundException extends RootBusinessException {

    private static final String MESSAGE = "해당하는 예약이 존재하지 않습니다.";

    public ReservationNotFoundException() {
        super(MESSAGE);
    }
}
