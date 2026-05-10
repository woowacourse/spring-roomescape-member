package roomescape.exception;

public class DuplicatedReservationRequestException extends BaseCustomException {
    public DuplicatedReservationRequestException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
