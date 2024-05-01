package roomescape.exception.reservation;

public class IllegalReservationFormatException extends RuntimeException {

    public IllegalReservationFormatException() {
        super("잘못된 형식의 예약 정보를 입력하셨습니다.");
    }
}
