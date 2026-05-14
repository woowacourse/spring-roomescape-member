package roomescape.theme.presentation.dto.request;

import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.validation.RequestValidator;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnail
) {
    public ThemeCreateRequest {
        RequestValidator.requireNotBlank(name, ThemeErrorCode.THEME_NAME_REQUIRED);
        RequestValidator.requireNotBlank(description, ThemeErrorCode.THEME_DESCRIPTION_REQUIRED);
        RequestValidator.requireNotBlank(thumbnail, ThemeErrorCode.THEME_THUMBNAIL_REQUIRED);
    }
}
