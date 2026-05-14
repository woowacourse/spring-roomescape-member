package roomescape.theme.presentation.dto.request;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ThemeErrorCode;
import roomescape.global.exception.customException.BadRequestException;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnail
) {
    public ThemeCreateRequest {
        validateNameNotEmpty(name);
        validateDescriptionNotEmpty(description);
        validateThumbnailNotEmpty(thumbnail);
    }

    private static void validateNameNotEmpty(String name) {
        validateStringValueNotEmpty(name, ThemeErrorCode.THEME_NAME_REQUIRED);
    }

    private static void validateDescriptionNotEmpty(String description) {
        validateStringValueNotEmpty(description, ThemeErrorCode.THEME_DESCRIPTION_REQUIRED);
    }

    private static void validateThumbnailNotEmpty(String thumbnail) {
        validateStringValueNotEmpty(thumbnail, ThemeErrorCode.THEME_THUMBNAIL_REQUIRED);
    }

    private static void validateStringValueNotEmpty(String description, ErrorCode errorCode) {
        if (description == null || description.trim().isBlank()) {
            throw new BadRequestException(errorCode);
        }
    }
}
