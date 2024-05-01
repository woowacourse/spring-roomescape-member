package roomescape.exception.reservationtime;

public class IllegalReservationTimeFormatException extends RuntimeException {

    public IllegalReservationTimeFormatException() {
        super("잘못된 시간 정보를 입력하셨습니다.");
    }
}
