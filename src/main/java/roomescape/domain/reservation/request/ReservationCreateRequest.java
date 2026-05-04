package roomescape.domain.reservation.request;

import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,
        LocalDate date,
        Long timeId
) {
}
