package roomescape.global.exception.validation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class ThemeNotFoundException extends ReservationException {

    public ThemeNotFoundException() {
        super(ErrorCode.THEME_NOT_FOUND, ErrorCode.THEME_NOT_FOUND.getMessage());
    }
}
