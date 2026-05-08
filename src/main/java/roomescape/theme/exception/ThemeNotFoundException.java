package roomescape.theme.exception;

import roomescape.exception.exception.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {

    public ThemeNotFoundException() {
        super(ThemeErrorCode.THEME_NOT_FOUND.getMessage());
    }

}
