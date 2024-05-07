package roomescape.exception;

public class PastReservationException extends RuntimeException {

    public PastReservationException() {
        super("이미 지난 시간에 대해서는 예약을 추가할 수 없습니다.");
    }
}
