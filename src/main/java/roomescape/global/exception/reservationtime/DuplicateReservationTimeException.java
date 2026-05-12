package roomescape.global.exception.reservationtime;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class DuplicateReservationTimeException extends RoomescapeException {

    public DuplicateReservationTimeException() {
        super(ErrorCode.DUPLICATE_RESERVATION_TIME, "이미 등록된 예약 시간입니다.");
    }
}
