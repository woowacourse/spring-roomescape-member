package roomescape.dto.reservationTime;

import jakarta.validation.constraints.NotNull;

public record AvailableReservationTimeRequest(
        @NotNull
        String date,
        long themeId
) {
}
