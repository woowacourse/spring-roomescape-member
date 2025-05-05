package roomescape.theme.exception;

import roomescape.globalException.BadRequestException;

public class InvalidThemeException extends BadRequestException {

    public InvalidThemeException(String message) {
        super(message);
    }
}
