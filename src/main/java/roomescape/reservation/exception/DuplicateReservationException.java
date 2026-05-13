package roomescape.reservation.exception;

public class DuplicateReservationException extends RuntimeException {
    public DuplicateReservationException() {
        super("중복 예약은 불가합니다.");
    }
}
