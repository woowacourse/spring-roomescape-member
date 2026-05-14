package roomescape.exception.theme;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ThemeNotFoundException extends BaseException {
    public ThemeNotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
