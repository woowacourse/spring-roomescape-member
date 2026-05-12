package roomescape.domain.theme.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class ThemeDeleteConflictException extends BusinessException {
    public ThemeDeleteConflictException(Throwable cause) {
        super(ErrorCode.THEME_DELETE_CONFLICT, cause);
    }
}
