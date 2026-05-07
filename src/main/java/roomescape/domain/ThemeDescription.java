package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ThemeDescription(String value) {

    public ThemeDescription {
        if (value == null || value.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_THEME_DESCRIPTION);
        }
    }
}
