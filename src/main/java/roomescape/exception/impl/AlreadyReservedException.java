package roomescape.exception.impl;

import roomescape.exception.RootException;

public class AlreadyReservedException extends RootException {

    private static final String MESSAGE = "이미 예약이 있습니다.";

    public AlreadyReservedException() {
        super(MESSAGE);
    }
}

