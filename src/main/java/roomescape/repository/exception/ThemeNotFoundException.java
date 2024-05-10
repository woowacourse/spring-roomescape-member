package roomescape.repository.exception;

import roomescape.exception.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {

    public ThemeNotFoundException(final String message) {
        super(message);
    }
}
