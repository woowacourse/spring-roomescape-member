package roomescape.theme.exception;

import roomescape.global.exception.exception.ResourceNotFoundException;

public class ThemeResourceNotFoundException extends ResourceNotFoundException {
    public ThemeResourceNotFoundException() {
        super(ThemeErrorCode.THEME_NOT_FOUND.getMessage());
    }
}
