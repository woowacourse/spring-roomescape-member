package roomescape.exception;

public class DuplicateReservationException extends RuntimeException {
    public DuplicateReservationException() {
        super("중복된 예약이 존재합니다.");
    }
}
