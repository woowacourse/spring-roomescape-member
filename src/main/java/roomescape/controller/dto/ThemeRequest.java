package roomescape.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.controller.ValidationMessage;

public record ThemeRequest(

        @NotBlank(message = ValidationMessage.THEME_NAME_IS_BLANK)
        @Size(max = 255, message = ValidationMessage.THEME_NAME_OVER_MAX_LENGTH)
        String name,

        @NotBlank(message = ValidationMessage.DESCRIPTION_IS_BLANK)
        @Size(max = 255, message = ValidationMessage.DESCRIPTION_OVER_MAX_LENGTH)
        String description,

        @NotBlank(message = ValidationMessage.THUMBNAIL_IS_BLANK)
        @Size(max = 255, message = ValidationMessage.THUMBNAIL_OVER_MAX_LENGTH)
        String thumbnail
) {
}
