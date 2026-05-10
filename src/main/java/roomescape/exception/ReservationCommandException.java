package roomescape.exception;

public class ReservationCommandException extends BaseCustomException{
    public ReservationCommandException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
