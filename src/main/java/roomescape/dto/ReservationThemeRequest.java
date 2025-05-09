package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservationThemeRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String thumbnail) {
}
