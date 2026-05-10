package roomescape.exception;

public class ReservationException extends BaseCustomException {
    public ReservationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
