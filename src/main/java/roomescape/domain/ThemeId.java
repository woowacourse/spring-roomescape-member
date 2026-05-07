package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ThemeId(Long value) {

    public ThemeId {
        if (value == null) {
            throw new DomainException(ErrorCode.INVALID_THEME_ID);
        }
    }
}
