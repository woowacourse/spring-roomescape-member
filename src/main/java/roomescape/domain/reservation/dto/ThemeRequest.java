package roomescape.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.domain.reservation.entity.Theme;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @Size(max = 255) @NotBlank String thumbnail
) {

    public Theme toEntity() {
        return Theme.withoutId(name, description, thumbnail);
    }
}
