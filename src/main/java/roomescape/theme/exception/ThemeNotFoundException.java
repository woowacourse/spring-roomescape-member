package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ThemeNotFoundException extends RoomescapeException {
    public ThemeNotFoundException() {
        super(HttpStatus.NOT_FOUND, ThemeErrorCode.THEME_NOT_FOUND);
    }
}
