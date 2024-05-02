package roomescape.exception;

public class ReservationTimeAlreadyExistsException extends AlreadyExistsException {

    public ReservationTimeAlreadyExistsException(String message) {
        super(String.format("%s에 해당하는 시간이 있습니다.", message));
    }
}
