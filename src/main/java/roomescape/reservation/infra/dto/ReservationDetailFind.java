package roomescape.reservation.infra.dto;

import roomescape.reservation.presentation.dto.response.ThemeFindResponse;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

import java.time.LocalDate;

public record ReservationDetailFind(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse themeFindResponse,
        TimeInformation timeInformation
) {
}
