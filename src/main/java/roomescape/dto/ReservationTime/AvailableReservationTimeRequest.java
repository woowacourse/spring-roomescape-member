package roomescape.dto.ReservationTime;

import jakarta.validation.constraints.NotNull;

public record AvailableReservationTimeRequest(
        @NotNull
        String date,
        long themeId
) {
}
