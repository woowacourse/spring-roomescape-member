package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class AlreadyReservedException extends RootBusinessException {

    private static final String MESSAGE = "이미 예약이 있습니다.";

    public AlreadyReservedException() {
        super(MESSAGE);
    }
}

