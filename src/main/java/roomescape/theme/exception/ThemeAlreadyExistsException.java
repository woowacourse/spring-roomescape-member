package roomescape.theme.exception;

import roomescape.global.exception.exception.AlreadyExistsException;

public class ThemeAlreadyExistsException extends AlreadyExistsException {
    public ThemeAlreadyExistsException() {
        super(ThemeErrorCode.THEME_DUPLICATE.getMessage());
    }
}
