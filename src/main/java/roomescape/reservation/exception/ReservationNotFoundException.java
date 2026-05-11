package roomescape.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("존재하지 않는 예약입니다. id=" + id);
    }

}
