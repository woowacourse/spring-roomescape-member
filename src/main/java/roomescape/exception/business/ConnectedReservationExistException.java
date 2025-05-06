package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ConnectedReservationExistException extends RootBusinessException {

    private static final String MESSAGE = "해당 정보를 사용하는 예약이 존재합니다.";

    public ConnectedReservationExistException() {
        super(MESSAGE);
    }
}
