package roomescape.theme.exception;

import roomescape.common.exception.ExceptionInformation;
import roomescape.common.exception.RoomEscapeException;

public class ThemeException extends RoomEscapeException {
    public ThemeException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}
