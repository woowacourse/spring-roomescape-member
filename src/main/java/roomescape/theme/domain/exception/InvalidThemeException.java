package roomescape.theme.domain.exception;

import roomescape.global.exception.RoomEscapeException;

public class InvalidThemeException extends RoomEscapeException {
    public InvalidThemeException(String message) {
        super(message);
    }
}
