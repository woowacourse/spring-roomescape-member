package roomescape.controller.theme.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Theme;

public record CreateThemeRequest(
        @NotEmpty
        @NotNull
        String name,
        @NotNull
        String description,
        @NotNull
        String thumbnail) {

    public Theme toDomain() {
        return new Theme(name, description, thumbnail);
    }
}
