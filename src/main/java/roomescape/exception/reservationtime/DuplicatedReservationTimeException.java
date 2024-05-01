package roomescape.exception.reservationtime;

public class DuplicatedReservationTimeException extends RuntimeException {

    public DuplicatedReservationTimeException() {
        super("이미 존재하는 시간은 추가할 수 없습니다.");
    }
}
