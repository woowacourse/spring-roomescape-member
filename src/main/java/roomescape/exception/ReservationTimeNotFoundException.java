package roomescape.exception;

public class ReservationTimeNotFoundException extends RuntimeException {

    public ReservationTimeNotFoundException() {
        super("요청 받은 ID에 해당하는 예약 시간이 존재하지 않습니다.");
    }
}
