package roomescape.exception;

public class ReservationCommandException extends BaseCustomException{
    public ReservationCommandException(ErrorCode errorCode) {
        super(errorCode);
    }
}
