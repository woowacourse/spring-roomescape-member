package roomescape.domain.theme.exception;

import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

public class ThemeNotFoundException extends BusinessException {

    public ThemeNotFoundException() {
        super(ErrorCode.THEME_NOT_FOUND);
    }
}
