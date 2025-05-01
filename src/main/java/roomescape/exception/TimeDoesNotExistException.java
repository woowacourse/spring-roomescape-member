package roomescape.exception;

public class TimeDoesNotExistException extends CannotCreatedException {

    public TimeDoesNotExistException() {
        super("예약 시간을 찾을 수 없다.");
    }
}
