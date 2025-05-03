package roomescape.common.exception;

public class NotFoundThemeException extends NotFoundException{
    public NotFoundThemeException() {
    }

    public NotFoundThemeException(String message) {
        super(message);
    }
}
