package roomescape.reservation.exception;

public class PastReservationNotAllowedException extends RuntimeException {

    public PastReservationNotAllowedException() {
        super("과거로 예약할 수 없습니다.");
    }
}
