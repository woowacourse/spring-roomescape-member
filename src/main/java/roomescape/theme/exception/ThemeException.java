package roomescape.theme.exception;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;

public class ThemeException extends RoomescapeException {
    public ThemeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
