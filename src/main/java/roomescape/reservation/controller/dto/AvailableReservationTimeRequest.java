package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        LocalDate date,
        Long themeId
) {
}
