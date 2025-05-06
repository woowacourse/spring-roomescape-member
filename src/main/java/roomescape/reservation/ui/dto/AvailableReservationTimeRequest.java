package roomescape.reservation.ui.dto;

import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        LocalDate date,
        Long themeId
) {
}
