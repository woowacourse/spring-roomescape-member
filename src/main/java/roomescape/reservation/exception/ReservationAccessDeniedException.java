package roomescape.reservation.exception;

public class ReservationAccessDeniedException extends RuntimeException {

    public ReservationAccessDeniedException() {
        super("예약을 수정/삭제할 권한이 없습니다.");
    }
}
