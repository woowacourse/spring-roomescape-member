package roomescape.theme.exception;

import roomescape.global.exception.ValidationException;

public class InvalidThemeException extends ValidationException {

    public InvalidThemeException(final String message) {
        super(message);
    }
}
