package roomescape.presentation.dto;

import org.springframework.http.HttpStatus;
import roomescape.global.exception.BusinessException;

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
        validateStringValueNotEmpty(name, new BusinessException(HttpStatus.BAD_REQUEST, "테마 이름은 null 혹은 빈 값일 수 없습니다"));
    }

    private static void validateDescriptionNotEmpty(String description) {
        validateStringValueNotEmpty(description, new BusinessException(HttpStatus.BAD_REQUEST, "테마 설명은 null 혹은 빈 값일 수 없습니다"));
    }

    private static void validateThumbnailNotEmpty(String thumbnail) {
        validateStringValueNotEmpty(thumbnail, new BusinessException(HttpStatus.BAD_REQUEST, "테마 썸네일은 null 혹은 빈 값일 수 없습니다"));
    }

    private static void validateStringValueNotEmpty(String description, BusinessException reservationException) {
        if (description == null || description.trim().isBlank()) {
            throw reservationException;
        }
    }
}
