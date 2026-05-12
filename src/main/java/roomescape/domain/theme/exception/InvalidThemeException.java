package roomescape.domain.theme.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class InvalidThemeException extends BusinessException {

    public InvalidThemeException() {
        super(ErrorCode.INVALID_THEME, null);
    }
}
