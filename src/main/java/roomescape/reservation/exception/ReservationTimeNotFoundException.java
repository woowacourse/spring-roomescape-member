package roomescape.reservation.exception;

public class ReservationTimeNotFoundException extends RuntimeException {

    public ReservationTimeNotFoundException(Long id) {
        super("존재하지 않는 예약 시간입니다. id=" + id);
    }
    
}
