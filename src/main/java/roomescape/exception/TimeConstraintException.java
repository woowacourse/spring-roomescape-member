package roomescape.exception;

public class TimeConstraintException extends ConstraintException {

    private static final String message = "[ERROR] 해당 시간에 대한 예약이 존재해서 삭제할 수 없습니다.";

    public TimeConstraintException() {
        super(message);
    }
}
