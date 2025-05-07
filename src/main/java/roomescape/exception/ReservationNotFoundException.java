package roomescape.exception;

public class ReservationNotFoundException extends CustomException {

    private static final String message = "예약이 존재하지 않습니다.";

    public ReservationNotFoundException() {
        super(message);
    }
}
