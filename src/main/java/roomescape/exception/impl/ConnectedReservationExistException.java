package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ConnectedReservationExistException extends RootException {

    private static final String MESSAGE = "해당 정보를 사용하는 예약이 존재합니다.";

    public ConnectedReservationExistException() {
        super(MESSAGE);
    }
}
