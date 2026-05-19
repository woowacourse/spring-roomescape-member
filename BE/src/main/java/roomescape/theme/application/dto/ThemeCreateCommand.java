package roomescape.theme.application.dto;

import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.validation.ValidationUtils;

public record ThemeCreateCommand(
        String name,
        String description,
        String thumbnail
) {
    public ThemeCreateCommand {
        ValidationUtils.requireNotBlank(name, ThemeErrorCode.THEME_NAME_REQUIRED);
        ValidationUtils.requireNotBlank(description, ThemeErrorCode.THEME_DESCRIPTION_REQUIRED);
        ValidationUtils.requireNotBlank(thumbnail, ThemeErrorCode.THEME_THUMBNAIL_REQUIRED);
    }
}
