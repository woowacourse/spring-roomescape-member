package roomescape.theme.exception;

import roomescape.common.exception.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {
    public ThemeNotFoundException(final String message) {
        super(message);
    }
}
