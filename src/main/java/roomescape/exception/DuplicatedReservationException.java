package roomescape.exception;

public class DuplicatedReservationException extends RuntimeException {

    public DuplicatedReservationException() {
        super("해당 시간에는 이미 예약이 존재합니다.");
    }
}
