package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ReservationThemeRequestDto(
        @NotEmpty String name,
        @NotEmpty String description,
        @NotEmpty String thumbnail
) {
}
