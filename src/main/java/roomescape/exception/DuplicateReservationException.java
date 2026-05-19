package roomescape.exception;

public class DuplicateReservationException extends CodeException {

    public DuplicateReservationException(String message) {
        super(ErrorCode.DUPLICATE_RESERVATION, message);
    }
}
