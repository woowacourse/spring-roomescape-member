package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ConnectedReservationExistException extends RootException {

    private static final String MESSAGE = "예약이 존재하는 예약 시간을 삭제할 수 없습니다.";

    public ConnectedReservationExistException() {
        super(MESSAGE);
    }
}
