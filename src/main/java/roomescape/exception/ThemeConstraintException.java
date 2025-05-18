package roomescape.exception;

public class ThemeConstraintException extends ConstraintException {

    private static final String message = "[ERROR] 해당 테마에 대한 예약이 존재해서 삭제할 수 없습니다.";

    public ThemeConstraintException() {
        super(message);
    }
}
