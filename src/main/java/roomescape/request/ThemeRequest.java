package roomescape.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String thumbnailUrl) {

    public Theme toDomainForSave() {
        return new Theme(null, name, description, thumbnailUrl);
    }
}
