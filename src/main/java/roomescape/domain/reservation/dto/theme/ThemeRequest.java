package roomescape.domain.reservation.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ThemeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @Size(max = 255) @NotBlank String thumbnail) {

}
