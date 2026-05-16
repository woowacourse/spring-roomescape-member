package roomescape.global.exception.theme;

import roomescape.global.exception.status.BadRequestException;

public class InvalidThemeException extends BadRequestException {
    public InvalidThemeException(String message) {
        super(message);
    }
}
