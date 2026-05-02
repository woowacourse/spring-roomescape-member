package roomescape.repository.entity;

import java.time.LocalDate;

public record ReservationWithTimeEntity(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeEntity time
) {
}
