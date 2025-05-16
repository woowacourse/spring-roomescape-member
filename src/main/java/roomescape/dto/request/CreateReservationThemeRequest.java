package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.ReservationTheme;

public record CreateReservationThemeRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String thumbnail
) {

    public ReservationTheme toReservationTheme() {
        return new ReservationTheme(name, description, thumbnail);
    }
}
