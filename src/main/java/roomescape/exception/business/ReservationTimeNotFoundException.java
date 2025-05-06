package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ReservationTimeNotFoundException extends RootBusinessException {

    private static final String MESSAGE = "해당하는 예약 시간을 찾을 수 없습니다.";

    public ReservationTimeNotFoundException() {
        super(MESSAGE);
    }
}
