package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,
        LocalDate date,
        long timeId
) {
}
