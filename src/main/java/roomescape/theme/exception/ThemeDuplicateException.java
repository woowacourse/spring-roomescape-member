package roomescape.theme.exception;

import roomescape.global.exception.exception.DuplicateException;

public class ThemeDuplicateException extends DuplicateException {
    public ThemeDuplicateException() {
        super(ThemeErrorCode.THEME_DUPLICATE.getMessage());
    }
}
