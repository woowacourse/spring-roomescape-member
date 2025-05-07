package roomescape.exception;

public class ReservationTimeNotFoundException extends CustomException {

    private static final String message = "예약시간이 존재하지 않습니다.";

    public ReservationTimeNotFoundException() {
        super(message);
    }
}
