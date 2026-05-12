package roomescape.theme.exception;

import roomescape.global.exception.exception.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {
    public ThemeNotFoundException() {
        super(ThemeErrorCode.THEME_NOT_FOUND.getMessage());
    }
}
