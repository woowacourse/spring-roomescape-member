package roomescape.theme.exception;

import roomescape.global.exception.exception.ResourceInUseException;

public class ThemeInUseException extends ResourceInUseException {
    public ThemeInUseException() {
        super(ThemeErrorCode.THEME_CONSTRAINT.getMessage());
    }
}
