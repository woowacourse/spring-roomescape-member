package roomescape.theme.exception;

import roomescape.common.exception.ErrorCode;
import roomescape.common.exception.RoomEscapeException;

public class ThemeException extends RoomEscapeException {
    public ThemeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
