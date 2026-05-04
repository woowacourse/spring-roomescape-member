package roomescape.domain.dto;

import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
}
