package roomescape.presentation.dto;

import roomescape.global.exception.EntityNotFoundException;

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
        validateStringValueNotEmpty(name, new EntityNotFoundException("테마 이름을 입력해 주세요. name: %s"
                .formatted(name)
        ));
    }

    private static void validateDescriptionNotEmpty(String description) {
        validateStringValueNotEmpty(description, new EntityNotFoundException("테마 설명을 입력해 주세요. description: %s"
                .formatted(description))
        );
    }

    private static void validateThumbnailNotEmpty(String thumbnail) {
        validateStringValueNotEmpty(thumbnail, new EntityNotFoundException("테마 썸네일을 입력해 주세요. thumbnail: %s"
                .formatted(thumbnail))
        );
    }

    private static void validateStringValueNotEmpty(String description, EntityNotFoundException reservationException) {
        if (description == null || description.trim().isBlank()) {
            throw reservationException;
        }
    }
}
