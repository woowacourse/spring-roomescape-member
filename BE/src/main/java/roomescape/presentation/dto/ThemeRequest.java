package roomescape.presentation.dto;


import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.InvalidRequestException;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
    public ThemeRequest {
        validateNameNotEmpty(name);
        validateDescriptionNotEmpty(description);
        validateThumbnailNotEmpty(thumbnail);
    }

    private static void validateNameNotEmpty(String name) {
        validateStringValueNotEmpty(name, ErrorCode.THEME_NAME_EMPTY);
    }

    private static void validateDescriptionNotEmpty(String description) {
        validateStringValueNotEmpty(description, ErrorCode.THEME_DESCRIPTION_EMPTY);
    }

    private static void validateThumbnailNotEmpty(String thumbnail) {
        validateStringValueNotEmpty(thumbnail, ErrorCode.THEME_THUMBNAIL_EMPTY);
    }

    private static void validateStringValueNotEmpty(String value, ErrorCode errorCode) {
        if (value == null || value.trim().isBlank()) {
            throw new InvalidRequestException(errorCode);
        }
    }
}
