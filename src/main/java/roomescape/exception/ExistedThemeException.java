package roomescape.exception;

public class ExistedThemeException extends CustomException {

    private static final String message = "테마가 이미 존재합니다.";

    public ExistedThemeException() {
        super(message);
    }
}
