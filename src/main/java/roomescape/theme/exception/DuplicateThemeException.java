package roomescape.theme.exception;

import roomescape.global.exception.base.BusinessException;

public class DuplicateThemeException extends BusinessException {

    public DuplicateThemeException() {
        super(ThemeErrorPolicy.DUPLICATE_THEME);
    }
}
