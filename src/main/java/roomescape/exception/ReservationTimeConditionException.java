package roomescape.exception;

public class ReservationTimeConditionException extends BaseCustomException {
    public ReservationTimeConditionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
