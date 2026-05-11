package roomescape.global.exception.theme;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class ThemeNotFoundException extends RoomescapeException {

    public ThemeNotFoundException() {
        super(ErrorCode.THEME_NOT_FOUND, "존재하지 않는 테마입니다.");
    }

    public ThemeNotFoundException(String message) {
        super(ErrorCode.THEME_NOT_FOUND, message);
    }
}
