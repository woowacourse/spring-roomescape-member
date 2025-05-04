package roomescape.reservation.domain.exception;

public class PastReservationException extends RuntimeException {
    
    public PastReservationException() {
        super("[ERROR] 현재 시간 이후로 예약할 수 있습니다.");
    }
}
