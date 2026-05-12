package roomescape.reservationtime.exception;

import java.time.LocalTime;

public class ReservationTimeDuplicatedException extends RuntimeException {

    public ReservationTimeDuplicatedException(LocalTime startAt) {
        super("이미 등록된 예약 시간입니다. startAt=" + startAt);
    }

}
