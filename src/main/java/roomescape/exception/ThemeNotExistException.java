package roomescape.exception;

public class ThemeNotExistException extends EntityNotExistException {

    public ThemeNotExistException() {
        super("테마를 찾을 수 없다.");
    }
}
