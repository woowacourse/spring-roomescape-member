package roomescape.reservationtime.exception;

public class ReservationTimeNotFoundException extends RuntimeException {

    public static final String MESSAGE = "존재하지 않는 예약 시간입니다.";

    public ReservationTimeNotFoundException(Long id) {
        super(MESSAGE + " id=" + id);
    }
    
}
