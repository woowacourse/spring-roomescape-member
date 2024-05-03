package roomescape.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import roomescape.model.Theme;

public record ThemeSaveRequest(
        @NotBlank
        @Max(30)
        String name,

        @NotBlank
        @Max(255)
        String description,

        @NotBlank
        @Max(255)
        String thumbnail
) {

    public Theme toTheme() {
        return new Theme(name, description, thumbnail);
    }
}
