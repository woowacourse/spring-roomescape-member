package roomescape.exception;

public class PastDateTimeReservationException extends CustomException {

    private static final String message = "지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.";

    public PastDateTimeReservationException() {
        super(message);
    }
}
