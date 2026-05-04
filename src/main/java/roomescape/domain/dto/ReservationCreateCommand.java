package roomescape.domain.dto;

import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        ReservationTime time,
        Long themeId
) {
}
