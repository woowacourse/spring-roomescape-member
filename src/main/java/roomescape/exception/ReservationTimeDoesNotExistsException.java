package roomescape.exception;

public class ReservationTimeDoesNotExistsException extends CannotDeleteReservationTimeException {

    private static final String ERROR_MESSAGE = "존재하지 않는 예약 시간입니다.";

    public ReservationTimeDoesNotExistsException() {
        super(ERROR_MESSAGE);
    }
}
