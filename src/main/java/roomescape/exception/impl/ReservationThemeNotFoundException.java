package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ReservationThemeNotFoundException extends RootException {

    private static final String MESSAGE = "해당하는 테마를 찾을 수 없습니다.";

    public ReservationThemeNotFoundException() {
        super(MESSAGE);
    }
}
