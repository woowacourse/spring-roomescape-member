package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        String thumbnail
) {
    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
