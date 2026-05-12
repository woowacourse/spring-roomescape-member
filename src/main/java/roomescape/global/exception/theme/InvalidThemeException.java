package roomescape.global.exception.theme;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class InvalidThemeException extends RoomescapeException {
    public InvalidThemeException(String message) {
        super(ErrorCode.INVALID_THEME, message);
    }
}
