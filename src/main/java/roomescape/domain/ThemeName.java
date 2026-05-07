package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ThemeName(String value) {

    public ThemeName {
        if (value == null || value.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_THEME_NAME);
        }
    }
}
