package roomescape.exception;

public class TimeNotExistException extends EntityNotExistException {

    public TimeNotExistException() {
        super("예약 시간을 찾을 수 없다.");
    }
}
