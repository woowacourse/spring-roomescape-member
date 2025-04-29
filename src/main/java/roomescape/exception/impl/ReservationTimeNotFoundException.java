package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ReservationTimeNotFoundException extends RootException {

    private static final String MESSAGE = "해당하는 예약 시간을 찾을 수 없습니다.";


    public ReservationTimeNotFoundException() {
        super(MESSAGE);
    }
}
