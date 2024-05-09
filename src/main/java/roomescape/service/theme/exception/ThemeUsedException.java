package roomescape.service.theme.exception;

import roomescape.exception.BadRequestException;

public class ThemeUsedException extends BadRequestException {

    public ThemeUsedException(final String message) {
        super(message);
    }
}
