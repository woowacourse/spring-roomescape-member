package roomescape.exception;

public class ReservationDoesNotExistsException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "예약이 존재하지 않습니다.";

    public ReservationDoesNotExistsException() {
        super(ERROR_MESSAGE);
    }
}
