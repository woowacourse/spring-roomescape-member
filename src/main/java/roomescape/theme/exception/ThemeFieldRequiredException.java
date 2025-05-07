package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.RoomescapeException;

public class ThemeFieldRequiredException extends RoomescapeException {
    public ThemeFieldRequiredException() {
        super(HttpStatus.BAD_REQUEST, ThemeErrorCode.THEME_FIELD_REQUIRED);
    }
}
