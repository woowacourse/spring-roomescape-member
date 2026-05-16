package roomescape.global.exception.theme;

import roomescape.global.exception.status.NotFoundException;

public class ThemeNotFoundException extends NotFoundException {
    public ThemeNotFoundException(String message) {
        super(message);
    }
}
