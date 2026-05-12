package roomescape.theme.exception;

import roomescape.global.exception.base.BusinessException;

public class ThemeInUseException extends BusinessException {

    public ThemeInUseException() {
        super(ThemeErrorPolicy.THEME_IN_USE);
    }
}
