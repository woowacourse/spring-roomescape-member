package roomescape.reservation.repository.dto;

import roomescape.theme.dto.response.ThemeFindResponse;
import roomescape.reservationtime.dto.response.TimeInformation;

import java.time.LocalDate;

public record ReservationDetailFind(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse themeFindResponse,
        TimeInformation timeInformation
) {
}
