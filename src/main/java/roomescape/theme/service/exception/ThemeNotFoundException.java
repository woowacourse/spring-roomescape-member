package roomescape.theme.service.exception;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.code.RoomEscapeErrorCode;

public class ThemeNotFoundException extends RoomEscapeException{

    public ThemeNotFoundException(RoomEscapeErrorCode roomEscapeErrorCode) {
        super(roomEscapeErrorCode);
    }
}
