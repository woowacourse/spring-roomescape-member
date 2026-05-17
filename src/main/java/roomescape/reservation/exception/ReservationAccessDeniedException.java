package roomescape.reservation.exception;

import roomescape.common.exception.AccessDeniedException;

public class ReservationAccessDeniedException extends AccessDeniedException {

    public ReservationAccessDeniedException() {
        super("예약을 수정/삭제할 권한이 없습니다.");
    }
}
