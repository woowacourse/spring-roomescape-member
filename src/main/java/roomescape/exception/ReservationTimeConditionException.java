package roomescape.exception;

public class ReservationTimeConditionException extends BaseCustomException {
    public ReservationTimeConditionException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
