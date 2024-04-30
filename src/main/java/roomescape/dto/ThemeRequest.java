package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Theme;

public record ThemeRequest(@NotNull @NotBlank String name,
                           @NotNull @NotBlank String description,
                           @NotNull @NotBlank String thumbnail) {
    public Theme toDomain() {
        return new Theme(null, name, description, thumbnail);
    }
}
