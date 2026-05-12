package roomescape.exception;

public class DuplicatedReservationRequestException extends BaseCustomException {
    public DuplicatedReservationRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
