package roomescape.service.exception;

import roomescape.exception.CustomException;

public class ThemeNotFoundException extends CustomException {

    public ThemeNotFoundException(final String message) {
        super(message);
    }
}
