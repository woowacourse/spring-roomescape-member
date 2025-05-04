package roomescape.common.exception;

public class ThemeValidationException extends DomainValidationException{
    public ThemeValidationException() {
    }

    public ThemeValidationException(String message) {
        super(message);
    }
}
