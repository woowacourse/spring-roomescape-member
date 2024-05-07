package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        String thumbnail
) {
    public Theme toDomain() {
        return new Theme(name, description, thumbnail);
    }
}
