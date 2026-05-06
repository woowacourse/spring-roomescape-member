package roomescape.domain.reservation.request;

import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,
        Long themeId,
        LocalDate date,
        Long timeId
) {
}
