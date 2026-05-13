package roomescape.theme.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidThemeRequestValueException extends BusinessException {

    public InvalidThemeRequestValueException() {
        super(ThemeErrorPolicy.INVALID_THEME_REQUEST_VALUE);
    }
}
