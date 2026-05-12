package roomescape.exception;

public class CannotDeleteReservationTimeException extends IllegalArgumentException {

    private static final String DEFAULT_ERROR_MESSAGE = "예약 시간을 삭제할 수 없습니다. ";

    public CannotDeleteReservationTimeException(String additionalMessage) {
        super(DEFAULT_ERROR_MESSAGE + additionalMessage);
    }
}
