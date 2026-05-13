package roomescape.exception;

public class ThemeDoesNotExistsException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "테마가 존재하지 않습니다.";

    public ThemeDoesNotExistsException() {
        super(ERROR_MESSAGE);
    }
}
