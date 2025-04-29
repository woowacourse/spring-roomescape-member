package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ReservationNotFoundException extends RootException {

    private static final String MESSAGE = "해당하는 예약이 존재하지 않습니다.";

    public ReservationNotFoundException() {
        super(MESSAGE);
    }
}
