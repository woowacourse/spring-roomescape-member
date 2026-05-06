package roomescape.presentation.dto;


import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationException;

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
        validateStringValueNotEmpty(name, new ReservationException(ErrorCode.THEME_REQUEST_NAME_NULL));
    }

    private static void validateDescriptionNotEmpty(String description) {
        ReservationException reservationException = new ReservationException(ErrorCode.THEME_REQUEST_DESCRIPTION_NULL);
        validateStringValueNotEmpty(description, reservationException);
    }

    private static void validateThumbnailNotEmpty(String thumbnail) {
        validateStringValueNotEmpty(thumbnail, new ReservationException(ErrorCode.THEME_REQUEST_THUMBNAIL_NULL));
    }

    private static void validateStringValueNotEmpty(String description, ReservationException reservationException) {
        if (description == null || description.trim().isBlank()) {
            throw reservationException;
        }
    }
}
