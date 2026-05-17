package roomescape.reservationtime.exception;

import roomescape.common.exception.DuplicatedException;

public class ReservationTimeDuplicatedException extends DuplicatedException {

    public ReservationTimeDuplicatedException() {
        super("이미 존재하는 예약 시간입니다.");
    }
}
