package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time
) {
}
