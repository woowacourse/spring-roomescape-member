package roomescape.exception;

public class InvalidReservationDateException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "예약 날짜 및 시간이 현재보다 과거일 수 없습니다.";

    public InvalidReservationDateException(final String message) {
        super(message);
    }

    public InvalidReservationDateException() {
        super(DEFAULT_MESSAGE);
    }
}
