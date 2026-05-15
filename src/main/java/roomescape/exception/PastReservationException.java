package roomescape.exception;

public class PastReservationException extends RuntimeException {

    public PastReservationException() {
        super("지나간 시간에 대해서는 예약생성이 불가능 합니다.");
    }
}
