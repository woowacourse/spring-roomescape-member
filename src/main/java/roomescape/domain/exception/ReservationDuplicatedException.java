package roomescape.domain.exception;

public class ReservationDuplicatedException extends IllegalArgumentException {

    private static final String message = "[ERROR] 해당 테마, 날짜, 시간에 대한 예약이 이미 존재합니다.";

    public ReservationDuplicatedException() {
        super(message);
    }
}
