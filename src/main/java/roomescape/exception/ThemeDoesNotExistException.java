package roomescape.exception;

public class ThemeDoesNotExistException extends CannotCreatedException {

    public ThemeDoesNotExistException() {
        super("테마를 찾을 수 없다.");
    }
}
