package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ThemeRequest(
        @NotBlank(message = "THEME_NAME_BLANK")
        @Size(min = 1, max = 20, message = "THEME_NAME_LENGTH_INVALID")
        String name,

        @NotBlank(message = "THEME_DESCRIPTION_BLANK")
        @Size(min = 1, max = 1000, message = "THEME_DESCRIPTION_LENGTH_INVALID")
        String description,

        @NotBlank(message = "THEME_URL_BLANK")
        String url) {
}
