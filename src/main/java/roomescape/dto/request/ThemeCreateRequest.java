package roomescape.dto.request;

import roomescape.exception.EmptyValueException;
import roomescape.exception.ExceptionCause;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnail
) {

    public ThemeCreateRequest {
        validateFields(name, description, thumbnail);
    }

    private void validateFields(String name, String description, String thumbnail) {
        if (name.isBlank()) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_THEME_NAME);
        }
        if (description.isBlank()) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_THEME_DESCRIPTION);
        }
        if (thumbnail.isBlank()) {
            throw new EmptyValueException(ExceptionCause.EMPTY_VALUE_THEME_THUMBNAIL);
        }
    }
}
