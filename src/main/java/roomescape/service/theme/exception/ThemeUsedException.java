package roomescape.service.theme.exception;

import roomescape.exception.BadRequestException;

public class ThemeUsedException extends BadRequestException {

    public ThemeUsedException(String message) {
        super(message);
    }
}
