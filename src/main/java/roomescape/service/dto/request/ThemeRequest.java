package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.theme.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        String thumbnail
) {
    public Theme toDomain() {
        if (thumbnail == null || thumbnail.isBlank()) {
            return new Theme(name, description);
        }
        return new Theme(name, description, thumbnail);
    }
}
