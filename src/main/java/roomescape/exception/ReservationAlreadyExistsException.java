package roomescape.exception;

public class ReservationAlreadyExistsException extends AlreadyExistsException {

    public ReservationAlreadyExistsException(String message) {
        super(String.format("%s 에 해당하는 예약이 있습니다.", message));
    }

}
