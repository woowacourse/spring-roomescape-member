package roomescape.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {

    public static final String MESSAGE = "존재하지 않는 예약입니다.";

    public ReservationNotFoundException(Long id) {
        super(MESSAGE + " id=" + id);
    }

    public ReservationNotFoundException(String name) {
        super(MESSAGE + " name=" + name);
    }

}
