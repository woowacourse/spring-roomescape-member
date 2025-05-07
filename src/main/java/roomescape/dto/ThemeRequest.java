package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.model.Theme;
import roomescape.model.ThemeName;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String thumbnail
) {
    public Theme toEntity() {
        return new Theme(
                null,
                new ThemeName(name),
                description,
                thumbnail);
    }
}
