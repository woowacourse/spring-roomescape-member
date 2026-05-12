package roomescape.theme.exception;

import roomescape.global.exception.base.BusinessException;

public class ThemeNotFoundException extends BusinessException {

    public ThemeNotFoundException() {
        super(ThemeErrorPolicy.THEME_NOT_FOUND);
    }
}
