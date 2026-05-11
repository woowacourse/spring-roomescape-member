package roomescape.global.exception.reservationtime;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class ReservationTimeNotFoundException extends RoomescapeException {

    public ReservationTimeNotFoundException() {
        super(ErrorCode.RESERVATION_TIME_NOT_FOUND, "존재하지 않는 예약 시간입니다.");
    }

    public ReservationTimeNotFoundException(String message) {
        super(ErrorCode.RESERVATION_TIME_NOT_FOUND, message);
    }
}
