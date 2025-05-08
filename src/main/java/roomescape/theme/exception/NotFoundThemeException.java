package roomescape.theme.exception;

import roomescape.globalException.NotFoundException;

public class NotFoundThemeException extends NotFoundException {

    public NotFoundThemeException(String message) {
        super(message);
    }
}
