package roomescape.theme.exception;

import roomescape.global.exception.base.BusinessException;

public class InvalidThemeRequestException extends BusinessException {

    public InvalidThemeRequestException() {
        super(ThemeErrorPolicy.INVALID_THEME_REQUEST);
    }
}
