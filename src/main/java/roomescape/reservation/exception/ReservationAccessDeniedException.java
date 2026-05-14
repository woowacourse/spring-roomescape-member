package roomescape.reservation.exception;

public class ReservationAccessDeniedException extends RuntimeException {

    public static final String MESSAGE = "본인 예약만 취소할 수 있습니다.";

    public ReservationAccessDeniedException(Long id) {
        super(MESSAGE + " id=" + id);
    }

}
