package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ThemeThumbnail(String value) {

    public ThemeThumbnail {
        if (value == null || value.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_THEME_THUMBNAIL);
        }
    }
}
