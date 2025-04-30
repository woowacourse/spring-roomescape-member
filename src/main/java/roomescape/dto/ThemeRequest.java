package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.model.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String thumbnail
) {
    public Theme toEntity() {
        return new Theme(
                null,
                name,
                description,
                thumbnail);
    }
}
