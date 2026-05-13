package roomescape.exception;

public class CannotDeleteReservationException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "예약을 삭제할 수 없습니다. ";

    public CannotDeleteReservationException(String additionalMessage) {
        super(ERROR_MESSAGE + additionalMessage);
    }
}
