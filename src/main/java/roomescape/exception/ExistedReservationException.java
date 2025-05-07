package roomescape.exception;

public class ExistedReservationException extends CustomException {

    private static final String message = "예약이 이미 존재합니다.";

    public ExistedReservationException() {
        super(message);
    }
}
