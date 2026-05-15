package roomescape.exception.theme;

import roomescape.exception.BaseException;
import roomescape.exception.ErrorCode;

public class ThemeAlreadyExistsException extends BaseException {
    public ThemeAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
